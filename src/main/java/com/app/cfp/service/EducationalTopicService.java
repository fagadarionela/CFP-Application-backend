package com.app.cfp.service;

import com.app.cfp.entity.EducationalTopic;
import com.app.cfp.repository.EducationalTopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EducationalTopicService {

    private final EducationalTopicRepository educationalTopicRepository;

    public List<EducationalTopic> getAllEducationalTopics() {
        return educationalTopicRepository.findAll();
    }
}
