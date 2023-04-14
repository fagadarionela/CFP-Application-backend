package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DifferentialDiagnosisGrade {
    @Id
    Long id;

    @ManyToOne
    @JoinColumn(name = "medical_case")
    MedicalCase medicalCase;

    @ManyToOne
    @JoinColumn(name = "differential_diagnosis_sign")
    DifferentialDiagnosisSign differentialDiagnosisSign;

    boolean checked;

    boolean correct;
}
