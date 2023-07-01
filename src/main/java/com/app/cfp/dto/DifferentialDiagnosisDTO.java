package com.app.cfp.dto;

import lombok.Data;

import java.util.List;

@Data
public class DifferentialDiagnosisDTO {

    private String diseaseName;

    private String name;

    private List<String> signs;
}
