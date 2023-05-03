package com.app.cfp.mapper;

import com.app.cfp.dto.MedicalCaseCustomizedDTO;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.utils.ImageUtility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {ImageUtility.class})
public interface MedicalCasesCustomizedMapper extends DataMapper<MedicalCase, MedicalCaseCustomizedDTO> {

    MedicalCase toDomain(MedicalCaseCustomizedDTO medicalCaseDTO);

    @Mapping(target = "CFPImage", expression = "java(" +
            "ImageUtility.decompressImage(medicalCase.getCFPImage()))")
    MedicalCaseCustomizedDTO toDto(MedicalCase medicalCase);
}
