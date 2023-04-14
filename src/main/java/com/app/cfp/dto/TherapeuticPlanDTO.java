package com.app.cfp.dto;

import lombok.Data;

import java.util.List;

@Data
public class TherapeuticPlanDTO {
    private String name;

    private List<String> methods;
}
