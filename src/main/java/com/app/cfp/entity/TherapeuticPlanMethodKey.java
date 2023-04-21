package com.app.cfp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TherapeuticPlanMethodKey implements Serializable {

    @Column(name = "therapeutic_plan")
    String therapeuticPlan;

    @Column(name = "method")
    String method;
}