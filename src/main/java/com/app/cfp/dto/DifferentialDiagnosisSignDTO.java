package com.app.cfp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DifferentialDiagnosisSignDTO {

    private UUID id;

    private DifferentialDiagnosisPartialDTO differentialDiagnosis;

    private SignDTO sign;
}
