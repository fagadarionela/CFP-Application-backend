package com.app.cfp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MedicalCase implements Comparable<MedicalCase> {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String encodedInfo;

    @NotNull
    private LocalDateTime allocationDate;

    @Column(length = 1000)
    private String additionalInformation;

    @Column(length = 1000)
    private String feedback;

    private int difficultyScore;

    @Column(nullable = false, length = 100000)
    private byte[] CFPImage;

    private String CFPImageName;

    @Column(length = 100000)
    private byte[] CFPImageCustomized;

    private String presumptiveDiagnosis;

    private String residentDiagnosis;

    @OneToMany(mappedBy = "medicalCase", fetch = FetchType.EAGER)
    private List<ClinicalSignGrade> clinicalSignGrades;

    @OneToMany(mappedBy = "medicalCase", fetch = FetchType.EAGER)
    private List<DifferentialDiagnosisGrade> differentialDiagnosisGrades;

    @OneToMany(mappedBy = "medicalCase", fetch = FetchType.EAGER)
    private List<TherapeuticPlanGrade> therapeuticPlanGrades;

    private boolean completedByResident;

    private boolean completedByExpert;

    private String correctDiagnosis;

    private boolean saved;

    private LocalDateTime insertDate;

    private LocalDateTime beginDate;

    private double score = 0;

    private double grade = 1;

    @Column(length = 1000)
    private String assigningReason = "";

    @Column(length = 1000)
    private String status = "";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident")
    private Resident resident;

    @Override
    public int compareTo(@NonNull MedicalCase o) {
        return this.allocationDate == null ?
                (o.allocationDate == null ? 0 : -1) :
                (o.allocationDate == null ? 1 : this.allocationDate.compareTo(o.allocationDate));
    }
}
