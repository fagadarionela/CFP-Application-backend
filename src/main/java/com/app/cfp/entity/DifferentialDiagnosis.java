package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class DifferentialDiagnosis {
    @Id
    private String name;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "differentialDiagnosisElements", nullable = false)
//    private List<Sign> differentialDiagnosisElements;

//    @Column(name="differentialDiagnosisElements")
//    @ElementCollection(targetClass=String.class)
//    private List<String> differentialDiagnosisElements;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "differentialDiagnosis")
    List<DifferentialDiagnosisSign> differentialDiagnosisSign;
}
