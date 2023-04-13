package com.app.cfp.dto;

import com.app.cfp.entity.Account;
import com.app.cfp.entity.MedicalCase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Data
public class ResidentDTO {

    private UUID id;

    private Account account;

}
