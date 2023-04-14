package com.app.cfp.mapper;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.GeneralClinicalSign;
import com.app.cfp.entity.Disease;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DifferentialDiagnosisMapper.class, TherapeuticPlanMapper.class},
        imports = {GeneralClinicalSign.class, Collectors.class})
public interface DiseaseMapper extends DataMapper<Disease, DiseaseDTO> {

    @Mapping(target = "generalClinicalSigns", expression = "java(" +
            "diseaseDTO.getGeneralClinicalSigns()" +
            ".stream().map(clinicalSign -> GeneralClinicalSign.builder().name(clinicalSign).build()).collect(Collectors.toSet()) )")
    Disease toDomain(DiseaseDTO diseaseDTO);


    @Mapping(target = "generalClinicalSigns", expression = "java(" +
            "disease.getGeneralClinicalSigns().stream().map(clinicalSign -> " +
            "clinicalSign.getName()).collect(Collectors.toList()))")
    DiseaseDTO toDto(Disease disease);
}
