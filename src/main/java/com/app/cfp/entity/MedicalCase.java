package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MedicalCase {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String encodedInfo;

    private Date birthDate;

    private String additionalInformation;

    private int difficultyScore;

    @Column(nullable = false, length = 100000)
    private byte[] CFPImage;

    private String presumptiveDiagnosis;

    private String diagnosis;

    private String residentDiagnosis;

    @OneToMany(mappedBy = "medicalCase", fetch = FetchType.EAGER)
    private List<ClinicalSignGrade> clinicalSignGrades;

    @OneToMany(mappedBy = "medicalCase", fetch = FetchType.EAGER)
    private List<DifferentialDiagnosisGrade> differentialDiagnosisGrades;

    @OneToMany(mappedBy = "medicalCase", fetch = FetchType.EAGER)
    private List<TherapeuticPlanGrade> therapeuticPlanGrades;

    private boolean completedByResident;

    private boolean completedByExpert;

    private boolean correctDiagnosis;

    private Date insertDate;

    private float score = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident", nullable = false)
    private Resident resident;
}
