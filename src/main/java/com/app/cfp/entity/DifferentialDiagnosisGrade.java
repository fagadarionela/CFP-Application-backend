package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DifferentialDiagnosisGrade {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medical_case")
    private MedicalCase medicalCase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="differentialDiagnosis", referencedColumnName="differential_diagnosis"),
            @JoinColumn(name="sign", referencedColumnName="sign")
    })
    private DifferentialDiagnosisSign differentialDiagnosisSign;

    boolean checked;

    boolean correct;
}
