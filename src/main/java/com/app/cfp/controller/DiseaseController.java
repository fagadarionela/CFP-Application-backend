package com.app.cfp.controller;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.Disease;
import com.app.cfp.mapper.DiseaseMapper;
import com.app.cfp.service.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/disease")
@AllArgsConstructor
@CrossOrigin
public class DiseaseController {

    private final DiseaseService diseaseService;

    private final DiseaseMapper diseaseMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<String> addDisease(@RequestBody DiseaseDTO diseaseDTO) {
        Disease disease = diseaseService.addDiagnosis(diseaseMapper.toDomain(diseaseDTO));

        if (disease != null) {
            return new ResponseEntity<>("The disease with id " + disease.getId() + " was created!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Can not add disease!", HttpStatus.CONFLICT);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_OPERATOR')")
    public ResponseEntity<Set<String>> getAllDiseases() {
        Set<String> diseases = diseaseService.getAllDiseases().stream().map(Disease::getName).collect(Collectors.toSet());
        return new ResponseEntity<>(diseases, HttpStatus.OK);
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_EXPERT')")
    public ResponseEntity<DiseaseDTO> getDiseaseByName(@PathVariable String name) {
        Disease disease = diseaseService.getDiseaseByName(name);
        return new ResponseEntity<>(diseaseMapper.toDto(disease), HttpStatus.OK);
    }
}
