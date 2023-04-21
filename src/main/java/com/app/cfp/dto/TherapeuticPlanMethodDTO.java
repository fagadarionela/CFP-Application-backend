package com.app.cfp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TherapeuticPlanMethodDTO {

    TherapeuticPlanPartialDTO therapeuticPlan;

    MethodDTO method;
}
