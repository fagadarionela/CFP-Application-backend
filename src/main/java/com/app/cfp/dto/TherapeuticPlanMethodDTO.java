package com.app.cfp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TherapeuticPlanMethodDTO {

    private UUID id;

    TherapeuticPlanPartialDTO therapeuticPlan;

    MethodDTO method;
}
