package com.app.cfp.mapper;

import com.app.cfp.dto.SignDTO;
import com.app.cfp.entity.Sign;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignMapper extends DataMapper<Sign, SignDTO> {

    Sign toDomain(SignDTO signDTO);

    SignDTO toDto(Sign sign);
}
