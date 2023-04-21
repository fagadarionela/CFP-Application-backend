package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DifferentialDiagnosisSignKey implements Serializable {

    @Column(name = "differential_diagnosis")
    String differentialDiagnosis;

    @Column(name = "sign")
    String sign;
}