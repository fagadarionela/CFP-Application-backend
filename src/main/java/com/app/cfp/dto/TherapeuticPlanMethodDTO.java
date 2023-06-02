package com.app.cfp.dto;

import lombok.Data;

@Data
public class TherapeuticPlanMethodDTO {

    private TherapeuticPlanPartialDTO therapeuticPlan;

    private MethodDTO method;
}
