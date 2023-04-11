package com.app.cfp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
public class MedicalCasePartialInformationDTO {

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
}
