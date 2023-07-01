package com.app.cfp.mapper;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DifferentialDiagnosisMapper.class, TherapeuticPlanMapper.class},
        imports = {ClinicalSign.class, EducationalTopic.class, Collectors.class, DifferentialDiagnosisSign.class, Sign.class, DifferentialDiagnosisSignKey.class})
public interface DiseaseMapper extends DataMapper<Disease, DiseaseDTO> {

    @Mapping(target = "clinicalSigns", expression = "java(" +
            "diseaseDTO.getClinicalSigns()" +
            ".stream().map(clinicalSign -> ClinicalSign.builder().name(clinicalSign).build()).collect(Collectors.toList()) )")
//    @Mapping(target = "educationalTopic", expression = "java(" +
//            "EducationalTopic.builder().name(diseaseDTO.getEducationalTopic()).build())")
//    @Mapping(target = "differentialDiagnosisSign", expression = "java(" +
//            "diseaseDTO.getDifferentialDiagnosis().stream().flatMap(\n" +
//            "                differentialDiagnosisDTO ->\n" +
//            "                        differentialDiagnosisDTO.getSigns().stream().map(\n" +
//            "                                sign ->\n" +
//            "                                        DifferentialDiagnosisSign.builder()\n" +
//            "                                                .id(new DifferentialDiagnosisSignKey(differentialDiagnosisDTO.getDiseaseName(), differentialDiagnosisDTO.getName(), sign))\n" +
//            "                                                .disease()"
//            "                                                .differentialDiagnosis(DifferentialDiagnosis.builder().name(differentialDiagnosisDTO.getName()).build())\n" +
//            "                                                .sign(Sign.builder().name(sign).build())\n" +
//            "                                                .build()\n" +
//            "                        )\n" +
//            "                ).collect(Collectors.toList()))")
    Disease toDomain(DiseaseDTO diseaseDTO);


    @Mapping(target = "clinicalSigns", expression = "java(" +
            "disease.getClinicalSigns().stream().map(clinicalSign -> " +
            "clinicalSign.getName()).collect(Collectors.toList()))")
//    @Mapping(target = "differentialDiagnosis", expression = "java(" +
//            "disease.getDifferentialDiagnosis().stream().filter(differentialDiagnosis -> " +
//            "differentialDiagnosis.getDifferentialDiagnosisSign().getId().getDisease().equals(disease.getName()))")
//    @Mapping(target = "educationalTopic", expression = "java(" +
//            "disease.getEducationalTopic().getName())")
    DiseaseDTO toDto(Disease disease);
}
