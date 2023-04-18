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
public class DifferentialDiagnosisSign {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "differential_diagnosis")
    private DifferentialDiagnosis differentialDiagnosis;

    @ManyToOne
    @JoinColumn(name = "sign")
    private Sign sign;
}
