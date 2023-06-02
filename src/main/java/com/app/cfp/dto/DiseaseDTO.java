package com.app.cfp.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiseaseDTO {

    private String name;

    private EducationalTopicDTO educationalTopic;

    private List<String> clinicalSigns;


    private List<DifferentialDiagnosisDTO> differentialDiagnosis;


    private List<TherapeuticPlanDTO> therapeuticPlans;

    private boolean retinalCondition;
}
