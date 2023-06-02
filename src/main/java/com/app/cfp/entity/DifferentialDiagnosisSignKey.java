package com.app.cfp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class DifferentialDiagnosisSignKey implements Serializable {

    @Column(name = "differential_diagnosis")
    String differentialDiagnosis;

    @Column(name = "sign")
    String sign;
}