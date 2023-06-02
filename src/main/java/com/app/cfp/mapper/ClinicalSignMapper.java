package com.app.cfp.mapper;

import com.app.cfp.dto.ClinicalSignDTO;
import com.app.cfp.entity.ClinicalSign;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicalSignMapper extends DataMapper<ClinicalSign, ClinicalSignDTO> {

    ClinicalSign toDomain(ClinicalSignDTO clinicalSignDTO);

    ClinicalSignDTO toDto(ClinicalSign clinicalSignDTO);
}
