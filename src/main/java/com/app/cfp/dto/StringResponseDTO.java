package com.app.cfp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StringResponseDTO {
    private String message;
}
