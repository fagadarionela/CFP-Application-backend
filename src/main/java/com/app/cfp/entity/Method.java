package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = "therapeuticPlanMethod")
public class Method {

    @Id
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "method")
    private List<TherapeuticPlanMethod> therapeuticPlanMethod;
}
