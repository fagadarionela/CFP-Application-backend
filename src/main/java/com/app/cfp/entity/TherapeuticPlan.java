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
public class TherapeuticPlan {
    @Id
    private String name;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "therapeuticPlanElements", nullable = false)
//    private List<Method> therapeuticPlanElements;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "therapeuticPlan")
    List<TherapeuticPlanMethod> therapeuticPlanMethod;

}
