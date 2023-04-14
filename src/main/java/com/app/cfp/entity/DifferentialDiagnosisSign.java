package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class DifferentialDiagnosisSign {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "differential_diagnosis")
    DifferentialDiagnosis differentialDiagnosis;

    @ManyToOne
    @JoinColumn(name = "sign")
    Sign sign;
}
