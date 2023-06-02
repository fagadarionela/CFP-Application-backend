package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@EqualsAndHashCode
//@IdClass(DifferentialDiagnosisSignKey.class)
public class DifferentialDiagnosisSign {

    @EmbeddedId
    private DifferentialDiagnosisSignKey id;

    @ManyToOne
    @MapsId("differentialDiagnosis")
    @JoinColumn(name = "differential_diagnosis")
    private DifferentialDiagnosis differentialDiagnosis;

    @ManyToOne
    @MapsId("sign")
    @JoinColumn(name = "sign")
    private Sign sign;
}
