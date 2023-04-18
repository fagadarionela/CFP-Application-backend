package com.app.cfp.mapper;

import com.app.cfp.dto.MedicalCaseCustomizedDTO;
import com.app.cfp.entity.MedicalCase;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalCasesCustomizedMapper extends DataMapper<MedicalCase, MedicalCaseCustomizedDTO> {

    MedicalCase toDomain(MedicalCaseCustomizedDTO medicalCaseDTO);

    MedicalCaseCustomizedDTO toDto(MedicalCase medicalCase);
}
