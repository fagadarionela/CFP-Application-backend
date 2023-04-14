package com.app.cfp.mapper;

import com.app.cfp.dto.TherapeuticPlanDTO;
import com.app.cfp.entity.Method;
import com.app.cfp.entity.TherapeuticPlanMethod;
import com.app.cfp.entity.TherapeuticPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {TherapeuticPlanMethod.class, Method.class, Collectors.class})
public interface TherapeuticPlanMapper extends DataMapper<TherapeuticPlan, TherapeuticPlanDTO> {

    @Mapping(target = "therapeuticPlanMethod", expression = "java(" +
            "therapeuticPlanDTO.getMethods().stream().map(\n" +
            "                therapeuticPlanMethod -> \n" +
            "                TherapeuticPlanMethod.builder()\n" +
            "                .therapeuticPlan(TherapeuticPlan.builder().name(therapeuticPlanDTO.getName()).build())\n" +
            "                .method(Method.builder().name(therapeuticPlanMethod).build())\n" +
            "                .build()).collect(Collectors.toList()) )")
    TherapeuticPlan toDomain(TherapeuticPlanDTO therapeuticPlanDTO);


    @Mapping(target = "methods", expression = "java(" +
            "therapeuticPlan.getTherapeuticPlanMethod()\n" +
            "            .stream().map(TherapeuticPlanMethod -> TherapeuticPlanMethod.getMethod().getName()).collect(Collectors.toList()) )")
    TherapeuticPlanDTO toDto(TherapeuticPlan therapeuticPlan);
}
