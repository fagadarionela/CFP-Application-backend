package com.app.cfp.mapper;

import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.VirtualCase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VirtualCaseMapper extends DataMapper<MedicalCase, VirtualCase> {

    @Mapping(ignore = true, target = "id")
    VirtualCase toVirtualCase(MedicalCase medicalCase);

    @Mapping(ignore = true, target = "id")
    MedicalCase toMedicalCase(VirtualCase virtualCase);
}
