package com.app.cfp.service;

import com.app.cfp.controller.handlers.exceptions.model.ResourceNotFoundException;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.Resident;
import com.app.cfp.repository.*;
import com.app.cfp.utils.UploadClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@AllArgsConstructor
public class MedicalCaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalCaseService.class);

    private final MedicalCaseRepository medicalCaseRepository;

    private final AllocationService allocationService;

    private final ClinicalSignGradeRepository clinicalSignGradeRepository;

    private final DifferentialDiagnosisSignRepository differentialDiagnosisSignRepository;

    private final DifferentialDiagnosisGradeRepository differentialDiagnosisGradeRepository;

    private final TherapeuticPlanMethodRepository therapeuticPlanMethodRepository;

    private final TherapeuticPlanGradeRepository therapeuticPlanGradeRepository;

    private final ResidentRepository residentRepository;

    private final UploadClient uploadClient;

    public Page<MedicalCase> getAllIncompleteCasesForResident(String username, Pageable pageable, String encodedInfo) {
        return medicalCaseRepository.findAllByResident_Account_UsernameAndCompletedByResidentFalseAndEncodedInfoContainsOrderByAllocationDateDesc(username, pageable, encodedInfo);
    }

    public Optional<MedicalCase> getMedicalCaseById(UUID id) {
        return medicalCaseRepository.findById(id);
    }


    public List<MedicalCase> getAllCasesForResident(String username) {
        return medicalCaseRepository.findAllByResident_Account_UsernameOrderByInsertDateDesc(username);
    }

    public Page<MedicalCase> getAllCompletedCasesForResident(String username, Pageable pageable, String diagnostic) {
        return medicalCaseRepository.findAllByResident_Account_UsernameAndCompletedByResidentTrueAndResidentDiagnosisContainsIgnoreCaseOrderByInsertDateDesc(username, pageable, diagnostic);
    }

    public List<MedicalCase> getAllCasesForResidentAllocatedInDate(String username, Date date) {
        return medicalCaseRepository.findAllByResident_Account_UsernameAndAllocationDate(username, date);
    }

    public Page<MedicalCase> getAllCompletedByResidentCases(Pageable pageable, String encodedInfo) {
        return medicalCaseRepository.findAllByCompletedByResidentTrueAndCompletedByExpertFalseAndEncodedInfoContainsOrderByInsertDateDesc(pageable, encodedInfo);
    }

    public Set<MedicalCase> getAllMedicalCasesAssignedTo(String encodedInfo) {
        return medicalCaseRepository.findAllByEncodedInfoOrderByInsertDateDesc(encodedInfo);
    }

    public MedicalCase addMedicalCase(MedicalCase medicalCase) {
        //Send medicalCase to DL algorithm
        //Set presumptive diagnosis and difficulty score
        if (medicalCase.getPresumptiveDiagnosis() == null || medicalCase.getPresumptiveDiagnosis().equals("")) {
            medicalCase.setPresumptiveDiagnosis("Presumtive diagnosis returned by DL algorithm");
        }
        medicalCase.setDifficultyScore(1);
        medicalCase.setInsertDate(LocalDateTime.now(ZoneOffset.UTC));
        medicalCase.setAllocationDate(LocalDateTime.now(ZoneOffset.UTC));
        medicalCase.setResidentDiagnosis("Normal");
        medicalCase.setCFPImageCustomizedName(null);

        return allocateCase(medicalCase);
    }

    public MedicalCase addMedicalCase(MultipartFile multipartFile, MedicalCase medicalCase) throws IOException {
        //Send medicalCase to DL algorithm
        //Set presumptive diagnosis and difficulty score
        if (medicalCase.getPresumptiveDiagnosis() == null || medicalCase.getPresumptiveDiagnosis().equals("")) {
            medicalCase.setPresumptiveDiagnosis("Presumtive diagnosis returned by DL algorithm");
        }

        medicalCase.setDifficultyScore(1);
        medicalCase.setInsertDate(LocalDateTime.now(ZoneOffset.UTC));
        medicalCase.setAllocationDate(LocalDateTime.now(ZoneOffset.UTC));
        medicalCase.setResidentDiagnosis("Normal");

        if (multipartFile != null && multipartFile.getBytes().length != 0) {
            String fileName = uploadClient.generateImageName(multipartFile.getOriginalFilename(), System.currentTimeMillis() + "");
            uploadImage(multipartFile, fileName);

            medicalCase.setCFPImageName(fileName);
        }

        medicalCase.setCFPImageCustomizedName(null);

        return allocateCase(medicalCase);
    }

    public MedicalCase addDrawingToMedicalCase(UUID medicalCaseId, MultipartFile multipartFile) {
        Optional<MedicalCase> actualMedicalCaseOptional = medicalCaseRepository.findById(medicalCaseId);
        if (actualMedicalCaseOptional.isEmpty()) {
            throw new ResourceNotFoundException(MedicalCase.class.getSimpleName() + " with id: " + medicalCaseId);
        }
        MedicalCase actualMedicalCase = actualMedicalCaseOptional.get();

//        actualMedicalCase.setCFPImageCustomized(image);

        String fileName = actualMedicalCase.getCFPImageCustomizedName() != null ? actualMedicalCase.getCFPImageCustomizedName() : uploadClient.generateImageName(actualMedicalCase.getCFPImageName(), System.currentTimeMillis() + "_" + actualMedicalCase.getResident().getId() + "_customized");

        uploadImage(multipartFile, fileName);

        actualMedicalCase.setCFPImageCustomizedName(fileName);
        return medicalCaseRepository.save(actualMedicalCase);
    }

    private MedicalCase allocateCase(MedicalCase medicalCase) {
        Resident allocatedResident = allocationService.allocateMedicalCase(medicalCase);
        medicalCase.setResident(allocatedResident);

        LOGGER.info("Assigning resident {} to medical case with diagnosis {}", allocatedResident, medicalCase.getPresumptiveDiagnosis());
        return medicalCaseRepository.saveAndFlush(medicalCase);
    }

    public MedicalCase updateMedicalCase(MedicalCase medicalCase) {
        Optional<MedicalCase> actualMedicalCaseOptional = medicalCaseRepository.findById(medicalCase.getId());

        if (actualMedicalCaseOptional.isEmpty()) {
            throw new ResourceNotFoundException(MedicalCase.class.getSimpleName() + " with id: " + medicalCase.getId());
        }
        MedicalCase actualMedicalCase = actualMedicalCaseOptional.get();

//        if (medicalCase.getCFPImage() != actualMedicalCase.getCFPImage()) {
//            medicalCase.setCFPImage(actualMedicalCase.getCFPImage());
//        }
//        if (medicalCase.getCFPImageCustomized() != actualMedicalCase.getCFPImageCustomized()) {
//            medicalCase.setCFPImageCustomized(actualMedicalCase.getCFPImageCustomized());
//        }
        medicalCase.setCFPImageName(actualMedicalCase.getCFPImageName());
//        int maxPoints = medicalCase.getTherapeuticPlanGrades().size() + medicalCase.getDifferentialDiagnosisGrades().size() + medicalCase.getClinicalSignGrades().size(); TODO
        int maxPoints = medicalCase.isAutomaticCase() ? medicalCase.getClinicalSignGrades().size() : medicalCase.getTherapeuticPlanGrades().size() + medicalCase.getDifferentialDiagnosisGrades().size() + medicalCase.getClinicalSignGrades().size();
        if (maxPoints != 0) {
            medicalCase.setGrade(Math.round(medicalCase.getScore() * 1000 / maxPoints) / 100.0);
        } else {
            if (medicalCase.getCorrectDiagnosis() != null && !medicalCase.getCorrectDiagnosis().equals(medicalCase.getResidentDiagnosis())) {
                medicalCase.setGrade(0);
            } else {
                medicalCase.setGrade(10);
            }
        }
        Resident resident = actualMedicalCase.getResident();
        if (medicalCase.isCompletedByExpert()) {
            List<MedicalCase> completeMedicalCases = resident.getMedicalCases().stream().filter(MedicalCase::isCompletedByExpert).toList();
            double sum = completeMedicalCases.stream().mapToDouble(MedicalCase::getGrade).sum() + medicalCase.getGrade();

            resident.setGrade(Math.round((sum * 100) / (completeMedicalCases.size() + 1)) / 100.0);
            residentRepository.save(resident);
        }
        medicalCase.setResident(resident);
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

    private String uploadImage(MultipartFile multipartFile, String fileName) {

        File file = new File(fileName);

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        uploadClient.uploadFile(file);

        file.delete();

        return fileName;
    }
}
