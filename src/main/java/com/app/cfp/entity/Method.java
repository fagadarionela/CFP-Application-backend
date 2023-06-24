package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString(exclude = "therapeuticPlanMethod")
public class Method {

    @Id
    @Column(length = 500)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "method")
    @Column(length = 500)
    private List<TherapeuticPlanMethod> therapeuticPlanMethod;
}
