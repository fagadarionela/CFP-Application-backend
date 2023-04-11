package com.app.cfp.mapper;

import com.app.cfp.dto.ResidentDTO;
import com.app.cfp.entity.Resident;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResidentsMapper extends DataMapper<Resident, ResidentDTO> {
    Resident toDomain(ResidentDTO residentDTO);

    ResidentDTO toDto(Resident resident);
}
