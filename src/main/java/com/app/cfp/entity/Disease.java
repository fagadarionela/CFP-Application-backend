package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Disease {

    @Id
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "educational_topic", nullable = false)
    private EducationalTopic educationalTopic;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "clinical_signs", nullable = false)
    private List<ClinicalSign> clinicalSigns;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "differential_diagnosis", nullable = false)
    private List<DifferentialDiagnosis> differentialDiagnosis;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "therapeutic_plans", nullable = false)
    private List<TherapeuticPlan> therapeuticPlans;

    private boolean retinalCondition;
}
