package com.app.cfp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DifferentialDiagnosisGradeDTO {

    private UUID id;

    private DifferentialDiagnosisSignDTO differentialDiagnosisSign;

    boolean checked;

    boolean correct;
}
