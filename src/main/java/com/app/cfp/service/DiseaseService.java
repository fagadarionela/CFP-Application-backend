package com.app.cfp.service;

import com.app.cfp.entity.Disease;
import com.app.cfp.repository.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DiseaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiseaseService.class);
    private final DiseaseRepository diseaseRepository;

    private final ClinicalSignRepository clinicalSignRepository;
    private final DifferentialDiagnosisRepository differentialDiagnosisRepository;

    private final DifferentialDiagnosisElementRepository differentialDiagnosisElementRepository;

    private final TherapeuticPlanRepository therapeuticPlanRepository;

    private final TherapeuticPlanElementRepository therapeuticPlanElementRepository;

    public Disease addDiagnosis(Disease disease) {
        clinicalSignRepository.saveAll(disease.getClinicalSigns());
        if (disease.getDifferentialDiagnosis() != null && !disease.getDifferentialDiagnosis().isEmpty()) {
            disease.getDifferentialDiagnosis().forEach(
                    differentialDiagnosis -> differentialDiagnosisElementRepository.saveAll(differentialDiagnosis.getDifferentialDiagnosisElements()));
            differentialDiagnosisRepository.saveAll(disease.getDifferentialDiagnosis());
        }
        if (disease.getTherapeuticPlans() != null && !disease.getTherapeuticPlans().isEmpty()) {
            disease.getTherapeuticPlans().forEach(
                    therapeuticPlan -> therapeuticPlanElementRepository.saveAll(therapeuticPlan.getTherapeuticPlanElements()));
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
