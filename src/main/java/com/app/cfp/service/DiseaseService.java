package com.app.cfp.service;

import com.app.cfp.entity.Disease;
import com.app.cfp.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DiseaseService {
    private final DiseaseRepository diseaseRepository;

    private final ClinicalSignRepository ClinicalSignRepository;
    private final DifferentialDiagnosisRepository differentialDiagnosisRepository;

    private final SignRepository signRepository;


    private final TherapeuticPlanRepository therapeuticPlanRepository;

    private final MethodRepository methodRepository;


    public Disease addDiagnosis(Disease disease) {
        ClinicalSignRepository.saveAll(disease.getClinicalSigns());
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

    public Disease getDiseaseByName(String name) {
        return diseaseRepository.findByName(name);
    }

}
