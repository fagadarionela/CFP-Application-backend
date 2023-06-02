package com.app.cfp.dto;

import com.app.cfp.entity.ClinicalSign;
import lombok.Data;

import java.util.List;

@Data
public class DiseaseFullDTO {

    private String name;

    private List<ClinicalSign> clinicalSigns;

    private List<DifferentialDiagnosisFullDTO> differentialDiagnosis;

    private List<TherapeuticPlanFullDTO> therapeuticPlans;
}
