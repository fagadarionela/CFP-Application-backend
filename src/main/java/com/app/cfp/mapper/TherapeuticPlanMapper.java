package com.app.cfp.mapper;

import com.app.cfp.dto.TherapeuticPlanDTO;
import com.app.cfp.entity.Method;
import com.app.cfp.entity.TherapeuticPlan;
import com.app.cfp.entity.TherapeuticPlanMethod;
import com.app.cfp.entity.TherapeuticPlanMethodKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {TherapeuticPlanMethod.class, Method.class, Collectors.class, TherapeuticPlanMethodKey.class})
public interface TherapeuticPlanMapper extends DataMapper<TherapeuticPlan, TherapeuticPlanDTO> {

    @Mapping(target = "therapeuticPlanMethod", expression = "java(" +
            "therapeuticPlanDTO.getMethods().stream().map(" +
            "                therapeuticPlanMethod -> " +
            "                TherapeuticPlanMethod.builder()" +
            "                .id(new TherapeuticPlanMethodKey(therapeuticPlanDTO.getName(), therapeuticPlanMethod))" +
            "                .therapeuticPlan(TherapeuticPlan.builder().name(therapeuticPlanDTO.getName()).build())" +
            "                .method(Method.builder().name(therapeuticPlanMethod).build())" +
            "                .build()).collect(Collectors.toList()) )")
    TherapeuticPlan toDomain(TherapeuticPlanDTO therapeuticPlanDTO);


    @Mapping(target = "methods", expression = "java(" +
            "therapeuticPlan.getTherapeuticPlanMethod()" +
            "            .stream().map(TherapeuticPlanMethod -> TherapeuticPlanMethod.getMethod().getName()).collect(Collectors.toList()) )")
    TherapeuticPlanDTO toDto(TherapeuticPlan therapeuticPlan);
}
