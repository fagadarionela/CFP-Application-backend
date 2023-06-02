package com.app.cfp.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class VirtualCase extends MedicalCase {

}
