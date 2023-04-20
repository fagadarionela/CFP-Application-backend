package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "idx_differential_diagnosis_sign", columnNames = {"disease", "differential_diagnosis", "sign"})
})
public class DiseaseDifferentialDiagnosisSign {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "disease")
    private Disease disease;

    @ManyToOne
    @JoinColumn(name = "differential_diagnosis")
    private DifferentialDiagnosis differentialDiagnosis;

    @ManyToOne
    @JoinColumn(name = "sign")
    private Sign sign;
}
