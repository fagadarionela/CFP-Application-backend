package com.app.cfp.mapper;

import com.app.cfp.dto.DifferentialDiagnosisDTO;
import com.app.cfp.entity.DifferentialDiagnosis;
import com.app.cfp.entity.DifferentialDiagnosisSign;
import com.app.cfp.entity.DifferentialDiagnosisSignKey;
import com.app.cfp.entity.Sign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {DifferentialDiagnosisSign.class, Sign.class, Collectors.class, DifferentialDiagnosisSignKey.class})
public interface DifferentialDiagnosisMapper extends DataMapper<DifferentialDiagnosis, DifferentialDiagnosisDTO> {

    @Mapping(target = "differentialDiagnosisSign", expression = "java(" +
            "differentialDiagnosisDTO.getSigns().stream().map(\n" +
            "                sign -> \n" +
            "                DifferentialDiagnosisSign.builder()\n" +
            "                .id(new DifferentialDiagnosisSignKey(differentialDiagnosisDTO.getName(), sign))" +
            "                .differentialDiagnosis(DifferentialDiagnosis.builder().name(differentialDiagnosisDTO.getName()).build())\n" +
            "                .sign(Sign.builder().name(sign).build())\n" +
            "                .build()).collect(Collectors.toList())) ")
    DifferentialDiagnosis toDomain(DifferentialDiagnosisDTO differentialDiagnosisDTO);


    @Mapping(target = "signs", expression = "java(" +
            "differentialDiagnosis.getDifferentialDiagnosisSign()\n" +
            "            .stream().map(differentialDiagnosisDifferentialDiagnosisSign -> differentialDiagnosisDifferentialDiagnosisSign.getSign().getName()).collect(Collectors.toList()) )")
    DifferentialDiagnosisDTO toDto(DifferentialDiagnosis differentialDiagnosis);
}
