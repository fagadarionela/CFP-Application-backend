package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Builder
public class DifferentialDiagnosisSign {

    @EmbeddedId
    DifferentialDiagnosisSignKey id;
    @ManyToOne
    @MapsId("differentialDiagnosis")
    @JoinColumn(name = "differential_diagnosis")
    private DifferentialDiagnosis differentialDiagnosis;

    @ManyToOne
    @MapsId("sign")
    @JoinColumn(name = "sign")
    private Sign sign;

    public DifferentialDiagnosisSign(DifferentialDiagnosisSignKey id, DifferentialDiagnosis differentialDiagnosis, Sign sign) {
        this.id = new DifferentialDiagnosisSignKey(differentialDiagnosis.getName(),sign.getName());
        this.differentialDiagnosis = differentialDiagnosis;
        this.sign = sign;
    }
}
