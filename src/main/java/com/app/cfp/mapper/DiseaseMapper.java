package com.app.cfp.mapper;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.ClinicalSign;
import com.app.cfp.entity.Disease;
import com.app.cfp.entity.EducationalTopic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DifferentialDiagnosisMapper.class, TherapeuticPlanMapper.class},
        imports = {ClinicalSign.class, EducationalTopic.class, Collectors.class})
public interface DiseaseMapper extends DataMapper<Disease, DiseaseDTO> {

    @Mapping(target = "clinicalSigns", expression = "java(" +
            "diseaseDTO.getClinicalSigns()" +
            ".stream().map(clinicalSign -> ClinicalSign.builder().name(clinicalSign).build()).collect(Collectors.toList()) )")
//    @Mapping(target = "educationalTopic", expression = "java(" +
//            "EducationalTopic.builder().name(diseaseDTO.getEducationalTopic()).build())")
    Disease toDomain(DiseaseDTO diseaseDTO);


    @Mapping(target = "clinicalSigns", expression = "java(" +
            "disease.getClinicalSigns().stream().map(clinicalSign -> " +
            "clinicalSign.getName()).collect(Collectors.toList()))")
//    @Mapping(target = "educationalTopic", expression = "java(" +
//            "disease.getEducationalTopic().getName())")
    DiseaseDTO toDto(Disease disease);
}
