package com.app.cfp.dto;

import com.app.cfp.utils.AuthorityType;
import lombok.Data;

@Data
public class AccountDTO {

    private String username;

    private String password;

    private AuthorityType role;
}
