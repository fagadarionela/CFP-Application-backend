package com.app.cfp.dto;

import com.app.cfp.entity.ClinicalSign;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DiseaseFullDTO {

    private UUID id;

    private String name;


    private List<ClinicalSign> clinicalSigns;


    private List<DifferentialDiagnosisFullDTO> differentialDiagnosis;


    private List<TherapeuticPlanFullDTO> therapeuticPlans;
}
