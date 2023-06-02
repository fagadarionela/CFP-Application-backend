package com.app.cfp.controller;

import com.app.cfp.dto.ClinicalSignDTO;
import com.app.cfp.mapper.ClinicalSignMapper;
import com.app.cfp.service.ClinicalSignService;
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
@RequestMapping(value = "/clinical-signs")
@AllArgsConstructor
@CrossOrigin
public class ClinicalSignController {

    private final ClinicalSignService clinicalSignService;

    private final ClinicalSignMapper clinicalSignMapper;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ClinicalSignDTO>> getAllClinicalSigns() {
        return new ResponseEntity<>(clinicalSignService.getAllClinicalSigns().stream().map(clinicalSignMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }
}
