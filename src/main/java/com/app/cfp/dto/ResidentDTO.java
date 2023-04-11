package com.app.cfp.dto;

import com.app.cfp.entity.Account;
import com.app.cfp.entity.MedicalCase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Data
public class ResidentDTO {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private Account account;

    @OneToMany(mappedBy = "resident", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MedicalCase> medicalCases;
}
