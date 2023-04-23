package com.app.cfp.mapper;

import com.app.cfp.dto.MethodDTO;
import com.app.cfp.entity.Method;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MethodMapper extends DataMapper<Method, MethodDTO> {

    Method toDomain(MethodDTO MethodDTO);

    MethodDTO toDto(Method Method);
}
