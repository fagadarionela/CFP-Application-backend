package com.app.cfp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ClinicalSignGradeDTO {

    private UUID id;

    private ClinicalSignDTO clinicalSign;

    boolean checked;

    boolean correct;
}
