package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = "differentialDiagnosisSign")
public class DifferentialDiagnosis {

    @Id
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "differentialDiagnosis")
    private List<DifferentialDiagnosisSign> differentialDiagnosisSign;
}
