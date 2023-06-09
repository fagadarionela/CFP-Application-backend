package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "differentialDiagnosisSign")
public class Sign {

    @Id
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "sign")
    private List<DifferentialDiagnosisSign> differentialDiagnosisSign;
}
