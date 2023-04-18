package com.app.cfp.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ResidentDTO {

    private UUID id;

    private AccountDTO account;

}
