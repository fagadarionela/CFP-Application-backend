package com.app.cfp.dto;

import lombok.Data;

import java.util.List;

@Data
public class DifferentialDiagnosisFullDTO {

    private String name;

    private List<SignDTO> signs;
}
