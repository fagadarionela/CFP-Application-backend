package com.app.cfp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TherapeuticPlanGradeDTO {
    private UUID id;

    private TherapeuticPlanMethodDTO therapeuticPlanMethod;

    boolean checked;

    boolean correct;
}
