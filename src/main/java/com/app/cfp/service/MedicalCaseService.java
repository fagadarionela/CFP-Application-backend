package com.app.cfp.service;

import com.app.cfp.controller.handlers.exceptions.model.ResourceNotFoundException;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.Resident;
import com.app.cfp.repository.*;
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

    private final ClinicalSignGradeRepository clinicalSignGradeRepository;

    private final DifferentialDiagnosisSignRepository differentialDiagnosisSignRepository;

    private final DifferentialDiagnosisGradeRepository differentialDiagnosisGradeRepository;

    private final TherapeuticPlanMethodRepository therapeuticPlanMethodRepository;

    private final TherapeuticPlanGradeRepository therapeuticPlanGradeRepository;

    public Page<MedicalCase> getAllIncompleteCasesForResident(String username, Pageable pageable, String encodedInfo) {
        return medicalCaseRepository.findAllByResident_Account_UsernameAndCompletedByResidentFalseAndEncodedInfoContains(username, pageable, encodedInfo);
    }

    public Page<MedicalCase> getAllCompletedCasesForResident(String username, Pageable pageable, String diagnostic) {
        return medicalCaseRepository.findAllByResident_Account_UsernameAndCompletedByResidentTrueAndResidentDiagnosisContainsIgnoreCase(username, pageable, diagnostic);
    }

    public Page<MedicalCase> getAllCompletedByResidentCases(Pageable pageable, String encodedInfo) {
        return medicalCaseRepository.findAllByCompletedByResidentTrueAndCompletedByExpertFalseAndEncodedInfoContains(pageable, encodedInfo);
    }

    public Set<MedicalCase> getAllMedicalCasesAssignedTo(String encodedInfo) {
        return medicalCaseRepository.findAllByEncodedInfoOrderByInsertDate(encodedInfo);
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
            medicalCase.setCFPImage(actualMedicalCase.getCFPImage());
        }
        medicalCase.setResident(actualMedicalCase.getResident());
        medicalCase.getClinicalSignGrades().forEach(clinicalSignGrade -> clinicalSignGrade.setMedicalCase(medicalCase));
        clinicalSignGradeRepository.saveAll(medicalCase.getClinicalSignGrades());

        medicalCase.getDifferentialDiagnosisGrades().forEach(differentialDiagnosisGrade -> {
            differentialDiagnosisGrade.setDifferentialDiagnosisSign(differentialDiagnosisSignRepository.findByDifferentialDiagnosisAndSign(differentialDiagnosisGrade.getDifferentialDiagnosisSign().getDifferentialDiagnosis(), differentialDiagnosisGrade.getDifferentialDiagnosisSign().getSign()));
            differentialDiagnosisGrade.setMedicalCase(medicalCase);
        });
        differentialDiagnosisGradeRepository.saveAll(medicalCase.getDifferentialDiagnosisGrades());
        medicalCase.getTherapeuticPlanGrades().forEach(therapeuticPlanGrade -> {
            therapeuticPlanGrade.setTherapeuticPlanMethod(therapeuticPlanMethodRepository.findByTherapeuticPlanAndMethod(therapeuticPlanGrade.getTherapeuticPlanMethod().getTherapeuticPlan(), therapeuticPlanGrade.getTherapeuticPlanMethod().getMethod()));
            therapeuticPlanGrade.setMedicalCase(medicalCase);
        });

        therapeuticPlanGradeRepository.saveAll(medicalCase.getTherapeuticPlanGrades());
        return medicalCaseRepository.save(medicalCase);
    }
}
