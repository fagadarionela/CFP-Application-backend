package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "medicalCase")
public class TherapeuticPlanGrade {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medical_case")
    private MedicalCase medicalCase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "therapeuticPlan", referencedColumnName = "therapeutic_plan"),
            @JoinColumn(name = "method", referencedColumnName = "method")
    })
    private TherapeuticPlanMethod therapeuticPlanMethod;

    boolean checked;

    boolean correct;
}
