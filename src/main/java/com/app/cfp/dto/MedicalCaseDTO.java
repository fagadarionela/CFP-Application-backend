package com.app.cfp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
public class MedicalCaseDTO {

    private UUID id;

    private String encodedInfo;

    private String additionalInformation;

    private int difficultyScore;

    @NotNull
    private byte[] CFPImage;

    private String presumptiveDiagnosis;

    private String residentDiagnosis;

    private String differentialDiagnosis;

    private String therapeuticPlan;

    private Date insertDate;

    private double score;
}
