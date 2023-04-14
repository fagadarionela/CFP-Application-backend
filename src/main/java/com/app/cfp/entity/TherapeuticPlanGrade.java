package com.app.cfp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TherapeuticPlanGrade {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "medical_case")
    MedicalCase medicalCase;

    @ManyToOne
    @JoinColumn(name = "therapeutic_plan_method")
    TherapeuticPlanMethod therapeuticPlanMethod;

    boolean checked;

    boolean correct;
}
