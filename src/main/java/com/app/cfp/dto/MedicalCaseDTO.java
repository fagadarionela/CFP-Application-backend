package com.app.cfp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Date;
import java.util.UUID;

@Data
public class MedicalCaseDTO {

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
}
