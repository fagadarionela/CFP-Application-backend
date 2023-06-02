package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class TherapeuticPlanMethod {

    @EmbeddedId
    private TherapeuticPlanMethodKey id;
//    = new TherapeuticPlanMethodKey(this.therapeuticPlan.getName(), this.method.getName());

    @ManyToOne
    @MapsId("therapeuticPlan")
    @JoinColumn(name = "therapeutic_plan")
    private TherapeuticPlan therapeuticPlan;

    @ManyToOne
    @MapsId("method")
    @JoinColumn(name = "method")
    private Method method;
}
