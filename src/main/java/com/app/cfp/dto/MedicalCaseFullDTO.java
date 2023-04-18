package com.app.cfp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class MedicalCaseFullDTO {

    private UUID id;

    private String encodedInfo;

    private Date birthDate;

    private String additionalInformation;

    private int difficultyScore;

    @NotNull
    private byte[] CFPImage;

    private String presumptiveDiagnosis;

    private String residentDiagnosis;

    private String differentialDiagnosis;

    private String therapeuticPlan;

    private Date insertDate;

    private float score;

    private ResidentDTO resident;

    private List<ClinicalSignGradeDTO> clinicalSignGrades;

    private List<DifferentialDiagnosisGradeDTO> differentialDiagnosisGrades;

    private List<TherapeuticPlanGradeDTO> therapeuticPlanGrades;

    private boolean completedByResident;

    private boolean completedByExpert;

    private boolean correctDiagnosis;
}
