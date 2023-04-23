package com.app.cfp.service;

import com.app.cfp.entity.Disease;
import com.app.cfp.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;

    private final ClinicalSignRepository clinicalSignRepository;
    private final DifferentialDiagnosisRepository differentialDiagnosisRepository;

    private final SignRepository signRepository;


    private final TherapeuticPlanRepository therapeuticPlanRepository;

    private final MethodRepository methodRepository;


    public Disease addDiagnosis(Disease disease) {
        if (diseaseRepository.findByName(disease.getName()) != null) {
            return null;
        }

        clinicalSignRepository.saveAll(disease.getClinicalSigns());
        if (disease.getDifferentialDiagnosis() != null && !disease.getDifferentialDiagnosis().isEmpty()) {
            disease.getDifferentialDiagnosis().forEach(differentialDiagnosis ->
                    differentialDiagnosis.getDifferentialDiagnosisSign().forEach(
                            differentialDiagnosisDifferentialDiagnosisElement -> signRepository.save(differentialDiagnosisDifferentialDiagnosisElement.getSign())));
            differentialDiagnosisRepository.saveAll(disease.getDifferentialDiagnosis());
        }
        if (disease.getTherapeuticPlans() != null && !disease.getTherapeuticPlans().isEmpty()) {
            disease.getTherapeuticPlans().forEach(therapeuticPlan ->
                    therapeuticPlan.getTherapeuticPlanMethod().forEach(
                            therapeuticPlanTherapeuticPlanElement -> methodRepository.save(therapeuticPlanTherapeuticPlanElement.getMethod())));
            therapeuticPlanRepository.saveAll(disease.getTherapeuticPlans());
        }

        return diseaseRepository.save(disease);
    }

    public List<Disease> getAllDiseases() {
        return diseaseRepository.findAll();
    }

    public void deleteDiseaseByName(String name) {
        diseaseRepository.deleteByName(name);
    }

    public Disease getDiseaseByName(String name) {
        return diseaseRepository.findByName(name);
    }

}
