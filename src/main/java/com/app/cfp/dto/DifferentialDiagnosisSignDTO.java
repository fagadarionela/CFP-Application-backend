package com.app.cfp.dto;

import lombok.Data;

@Data
public class DifferentialDiagnosisSignDTO {

    private DifferentialDiagnosisPartialDTO differentialDiagnosis;

    private SignDTO sign;
}
