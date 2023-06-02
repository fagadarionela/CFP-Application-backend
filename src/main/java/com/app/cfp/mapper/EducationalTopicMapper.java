package com.app.cfp.mapper;

import com.app.cfp.dto.EducationalTopicDTO;
import com.app.cfp.entity.EducationalTopic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EducationalTopicMapper extends DataMapper<EducationalTopic, EducationalTopicDTO> {

    EducationalTopic toDomain(EducationalTopicDTO educationalTopicDTO);

    EducationalTopicDTO toDto(EducationalTopic educationalTopic);
}
