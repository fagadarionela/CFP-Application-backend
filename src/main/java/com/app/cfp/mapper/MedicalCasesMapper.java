package com.app.cfp.mapper;

import com.app.cfp.dto.MedicalCaseDTO;
import com.app.cfp.entity.MedicalCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalCasesMapper extends DataMapper<MedicalCase, MedicalCaseDTO> {

    MedicalCase toDomain(MedicalCaseDTO medicalCaseDTO);

    MedicalCaseDTO toDto(MedicalCase medicalCase);
}
