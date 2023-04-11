package com.app.cfp.service;

import com.app.cfp.controller.handlers.exceptions.model.ResourceNotFoundException;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.Resident;
import com.app.cfp.repository.MedicalCaseRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MedicalCaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalCaseService.class);
    private final MedicalCaseRepository medicalCaseRepository;
    private final ResidentService residentService;

    public Page<MedicalCase>  getAllIncompleteCasesForResident(String username, Pageable pageable, String encodedInfo) {
        return medicalCaseRepository.findAllByResident_Account_UsernameAndCompletedFalseAndEncodedInfoContains(username, pageable, encodedInfo);
    }

    public Page<MedicalCase> getAllCompletedCasesForResident(String username, Pageable pageable, String diagnostic) {
        return medicalCaseRepository.findAllByResident_Account_UsernameAndCompletedTrueAndResidentDiagnosisContainsIgnoreCase(username, pageable, diagnostic);
    }

    public Page<MedicalCase> getAllCompletedCases(Pageable pageable) {
        return medicalCaseRepository.findAllByCompletedTrue(pageable);
    }

    public Set<MedicalCase> getAllMedicalCasesAssignedTo(String encodedInfo) {
        return medicalCaseRepository.findAllByEncodedInfoOrderByInsertDate(encodedInfo);
    }

    public MedicalCase getMedicalCase(UUID id) {
        return medicalCaseRepository.getReferenceById(id);
    }

    public MedicalCase addMedicalCase(MedicalCase medicalCase) {
        //Send medicalCase to DL algorithm
        //TODO
        //Set presumptive diagnosis and difficulty score
        if (medicalCase.getPresumptiveDiagnosis() == null || medicalCase.getPresumptiveDiagnosis().equals("")) {
            medicalCase.setPresumptiveDiagnosis("Presumtive diagnosis returned by DL algorithm");
        }
        medicalCase.setDifficultyScore(1);
        medicalCase.setInsertDate(new Date());

        return allocateCase(medicalCase);
    }

    private MedicalCase allocateCase(MedicalCase medicalCase) {
        List<Resident> residents = residentService.getAllResidents();
        Resident allocatedResident = residents.get(0); //TODO should be a call to allocationAlgorithm
//        allocatedResident.setMedicalCases(allocatedResident.getMedicalCases().add(medicalCase));
        medicalCase.setResident(allocatedResident);

        LOGGER.info("Assigning resident {} to medical case with id {}", allocatedResident, medicalCase.getId());
        return medicalCaseRepository.save(medicalCase);
    }

    public MedicalCase updateMedicalCase(MedicalCase medicalCase) {
        Optional<MedicalCase> actualMedicalCaseOptional = medicalCaseRepository.findById(medicalCase.getId());

        if (!actualMedicalCaseOptional.isPresent()) {
            throw new ResourceNotFoundException(MedicalCase.class.getSimpleName() + " with id: " + medicalCase.getId());
        }
        MedicalCase actualMedicalCase = actualMedicalCaseOptional.get();

        if (medicalCase.getCFPImage() != actualMedicalCase.getCFPImage()) {
//            throw new EntityValidationException(MedicalCase.class.getSimpleName() + " with id: " + medicalCase.getId(), Collections.singleton("You can not change the case!"));
            medicalCase.setCFPImage(actualMedicalCase.getCFPImage());
        }
        medicalCase.setResident(actualMedicalCase.getResident());
        return medicalCaseRepository.save(medicalCase);
    }

    public MedicalCase completeMedicalCase(UUID id) {
        Optional<MedicalCase> medicalCaseOptional = medicalCaseRepository.findById(id);

        if (medicalCaseOptional.isPresent()) {
            MedicalCase medicalCase = medicalCaseOptional.get();
            medicalCase.setCompleted(true);
            return medicalCaseRepository.save(medicalCase);
        }

        return null;
    }
}
