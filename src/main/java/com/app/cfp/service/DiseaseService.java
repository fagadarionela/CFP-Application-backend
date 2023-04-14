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

    private final GeneralClinicalSignRepository generalClinicalSignRepository;
    private final DifferentialDiagnosisRepository differentialDiagnosisRepository;

    private final SignRepository signRepository;

    private final DifferentialDiagnosisSignRepository differentialDiagnosisSignRepository;

    private final TherapeuticPlanRepository therapeuticPlanRepository;

    private final MethodRepository methodRepository;

    private final TherapeuticPlanMethodRepository therapeuticPlanMethodRepository;

    public Disease addDiagnosis(Disease disease) {
        generalClinicalSignRepository.saveAll(disease.getGeneralClinicalSigns());
        if (disease.getDifferentialDiagnosis() != null && !disease.getDifferentialDiagnosis().isEmpty()) {
            disease.getDifferentialDiagnosis().forEach(differentialDiagnosis ->
                    differentialDiagnosis.getDifferentialDiagnosisSign().forEach(
                            differentialDiagnosisDifferentialDiagnosisElement -> signRepository.save(differentialDiagnosisDifferentialDiagnosisElement.getSign())));
            differentialDiagnosisRepository.saveAll(disease.getDifferentialDiagnosis());
            disease.getDifferentialDiagnosis().forEach(
                    differentialDiagnosis -> differentialDiagnosisSignRepository.saveAll(differentialDiagnosis.getDifferentialDiagnosisSign())
            );

        }
        if (disease.getTherapeuticPlans() != null && !disease.getTherapeuticPlans().isEmpty()) {
//            disease.getTherapeuticPlans().forEach(
//                    therapeuticPlan -> therapeuticPlanElementRepository.saveAll(therapeuticPlan.getTherapeuticPlanElements()));
            disease.getTherapeuticPlans().forEach(therapeuticPlan ->
                    therapeuticPlan.getTherapeuticPlanMethod().forEach(
                            therapeuticPlanTherapeuticPlanElement -> methodRepository.save(therapeuticPlanTherapeuticPlanElement.getMethod())));
            therapeuticPlanRepository.saveAll(disease.getTherapeuticPlans());
            disease.getTherapeuticPlans().forEach(
                    therapeuticPlan -> therapeuticPlanMethodRepository.saveAll(therapeuticPlan.getTherapeuticPlanMethod())
            );

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
