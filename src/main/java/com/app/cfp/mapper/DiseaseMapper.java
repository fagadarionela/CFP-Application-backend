package com.app.cfp.mapper;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.ClinicalSign;
import com.app.cfp.entity.Disease;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DifferentialDiagnosisMapper.class, TherapeuticPlanMapper.class},
        imports = {ClinicalSign.class, Collectors.class})
public interface DiseaseMapper extends DataMapper<Disease, DiseaseDTO> {

    @Mapping(target = "clinicalSigns", expression = "java(" +
            "diseaseDTO.getClinicalSigns()" +
            ".stream().map(clinicalSign -> ClinicalSign.builder().name(clinicalSign).build()).collect(Collectors.toList()) )")
    Disease toDomain(DiseaseDTO diseaseDTO);


    @Mapping(target = "clinicalSigns", expression = "java(" +
            "disease.getClinicalSigns().stream().map(clinicalSign -> " +
            "clinicalSign.getName()).collect(Collectors.toList()))")
    DiseaseDTO toDto(Disease disease);
}
