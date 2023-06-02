package com.app.cfp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemDateDTO {
    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private int second;
}
