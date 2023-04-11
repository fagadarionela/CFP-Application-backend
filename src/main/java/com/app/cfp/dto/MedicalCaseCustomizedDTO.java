package com.app.cfp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
public class MedicalCaseCustomizedDTO {

    private UUID id;

    @NotNull
    private byte[] CFPImage;

    private Date insertDate;
}
