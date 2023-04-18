package com.app.cfp.dto;

import lombok.Data;

import java.util.List;

@Data
public class TherapeuticPlanFullDTO {
    private String name;

    private List<MethodDTO> methods;
}
