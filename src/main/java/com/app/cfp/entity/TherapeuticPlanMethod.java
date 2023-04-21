package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Builder
public class TherapeuticPlanMethod {

    @EmbeddedId
    TherapeuticPlanMethodKey id;

    @ManyToOne
    @MapsId("therapeuticPlan")
    @JoinColumn(name = "therapeutic_plan")
    private TherapeuticPlan therapeuticPlan;

    @ManyToOne
    @MapsId("method")
    @JoinColumn(name = "method")
    private Method method;

    public TherapeuticPlanMethod(TherapeuticPlanMethodKey id, TherapeuticPlan therapeuticPlan, Method method) {
        this.id = new TherapeuticPlanMethodKey(therapeuticPlan.getName(), method.getName());
        this.therapeuticPlan = therapeuticPlan;
        this.method = method;
    }
}
