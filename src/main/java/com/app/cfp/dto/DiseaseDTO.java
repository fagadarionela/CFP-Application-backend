package com.app.cfp.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DiseaseDTO {

    private String name;


    private List<String> clinicalSigns;


    private List<DifferentialDiagnosisDTO> differentialDiagnosis;


    private List<TherapeuticPlanDTO> therapeuticPlans;
}
