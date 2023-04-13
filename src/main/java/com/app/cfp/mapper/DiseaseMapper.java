package com.app.cfp.mapper;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.Disease;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiseaseMapper extends DataMapper<Disease, DiseaseDTO> {

    Disease toDomain(DiseaseDTO diseaseDTO);

    DiseaseDTO toDto(Disease disease);
}
