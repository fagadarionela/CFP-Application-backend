package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = "medicalCases")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "medicalCases")
public class Resident {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private Account account;

    @OneToMany(mappedBy = "resident", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SortedSet<MedicalCase> medicalCases = new TreeSet<>();

    private Double grade = 1.0d;
}
