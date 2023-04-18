package com.app.cfp.mapper;

import com.app.cfp.dto.MedicalCaseFullDTO;
import com.app.cfp.entity.MedicalCase;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalCasesMapper extends DataMapper<MedicalCase, MedicalCaseFullDTO> {

    MedicalCase toDomain(MedicalCaseFullDTO medicalCaseFullDTO);

    MedicalCaseFullDTO toDto(MedicalCase medicalCase);
}
