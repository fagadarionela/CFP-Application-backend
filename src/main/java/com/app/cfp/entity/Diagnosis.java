package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Diagnosis {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "clinicalSigns", nullable = false)
    private List<ClinicalSigns> clinicalSigns;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "differentialDiagnosis", nullable = false)
    private List<DifferentialDiagnosis> differentialDiagnosis;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "therapeuticPlan", nullable = false)
    private List<TherapeuticPlan> therapeuticPlan;
}
