package com.app.cfp.mapper;

import com.app.cfp.dto.ClinicalSignDTO;
import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.ClinicalSign;
import com.app.cfp.entity.Disease;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClinicalSignMapper extends DataMapper<ClinicalSign, ClinicalSignDTO> {

    ClinicalSign toDomain(ClinicalSignDTO clinicalSignDTO);

    ClinicalSignDTO toDto(ClinicalSign clinicalSignDTO);
}
