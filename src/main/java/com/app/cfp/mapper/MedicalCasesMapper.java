package com.app.cfp.mapper;

import com.app.cfp.dto.MedicalCaseFullDTO;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.utils.ImageUtility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {ImageUtility.class})
public interface MedicalCasesMapper extends DataMapper<MedicalCase, MedicalCaseFullDTO> {

    @Mapping(target = "CFPImage", expression = "java(" +
            "ImageUtility.compressImage(medicalCaseFullDTO.getCFPImage()))")
    @Mapping(target = "CFPImageCustomized", expression = "java(" +
            "ImageUtility.compressImage(medicalCaseFullDTO.getCFPImageCustomized()))")
    MedicalCase toDomain(MedicalCaseFullDTO medicalCaseFullDTO);

    @Mapping(target = "CFPImage", expression = "java(" +
            "ImageUtility.decompressImage(medicalCase.getCFPImage()))")
    @Mapping(target = "CFPImageCustomized", expression = "java(" +
            "ImageUtility.decompressImage(medicalCase.getCFPImageCustomized()))")
    MedicalCaseFullDTO toDto(MedicalCase medicalCase);
}
