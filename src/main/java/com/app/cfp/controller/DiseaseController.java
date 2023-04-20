package com.app.cfp.controller;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.entity.Disease;
import com.app.cfp.mapper.DiseaseMapper;
import com.app.cfp.service.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/disease")
@AllArgsConstructor
@CrossOrigin
public class DiseaseController {

    private final DiseaseService diseaseService;

    private final DiseaseMapper diseaseMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addDisease(@RequestBody DiseaseDTO diseaseDTO) {
        Disease disease = diseaseService.addDiagnosis(diseaseMapper.toDomain(diseaseDTO));

        if (disease != null) {
            return new ResponseEntity<>("The disease with name " + disease.getName() + " was created!", HttpStatus.CREATED);
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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<DiseaseDTO>> getAllDiseasesInfo() {
        List<Disease> diseases = diseaseService.getAllDiseases();

        return new ResponseEntity<>(diseases.stream().map(diseaseMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteDisease(@PathVariable("id") UUID id) {
        diseaseService.deleteDiseaseById(id);

        return new ResponseEntity<>("Successfully deleted the disease!", HttpStatus.OK);
    }
}
