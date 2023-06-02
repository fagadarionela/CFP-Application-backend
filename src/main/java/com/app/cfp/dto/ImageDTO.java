package com.app.cfp.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ImageDTO {
    @NotNull
    private String CFPImage;
}
