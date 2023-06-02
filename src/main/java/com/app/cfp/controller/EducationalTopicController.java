package com.app.cfp.controller;

import com.app.cfp.dto.EducationalTopicDTO;
import com.app.cfp.mapper.EducationalTopicMapper;
import com.app.cfp.service.EducationalTopicService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/educational-topic")
@AllArgsConstructor
@CrossOrigin
public class EducationalTopicController {

    private final EducationalTopicService educationalTopicService;

    private final EducationalTopicMapper educationalTopicMapper;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<EducationalTopicDTO>> getAllClinicalSigns() {
        return new ResponseEntity<>(educationalTopicService.getAllEducationalTopics().stream().map(educationalTopicMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }
}
