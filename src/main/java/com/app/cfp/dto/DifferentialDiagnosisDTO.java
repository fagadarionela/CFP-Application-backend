package com.app.cfp.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
public class DifferentialDiagnosisDTO {

    private String name;

    private List<DifferentialDiagnosisElementDTO> differentialDiagnosisElements;

}
