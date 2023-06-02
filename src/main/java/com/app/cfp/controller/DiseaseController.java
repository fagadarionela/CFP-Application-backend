package com.app.cfp.controller;

import com.app.cfp.dto.DiseaseDTO;
import com.app.cfp.dto.StringResponseDTO;
import com.app.cfp.entity.Disease;
import com.app.cfp.mapper.DiseaseMapper;
import com.app.cfp.service.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<StringResponseDTO> addDisease(@RequestBody DiseaseDTO diseaseDTO) {
        Disease disease = diseaseService.addDiagnosis(diseaseMapper.toDomain(diseaseDTO));

        if (disease != null) {
            return new ResponseEntity<>(StringResponseDTO.builder().message("The disease with name " + disease.getName() + " was added!").build(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(StringResponseDTO.builder().message("Can not add disease!").build(), HttpStatus.CONFLICT);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_OPERATOR', 'ROLE_EXPERT')")
    public ResponseEntity<List<DiseaseDTO>> getAllDiseases() {
        List<Disease> diseases = diseaseService.getAllDiseases();
        return new ResponseEntity<>(diseases.stream().map(diseaseMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
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

    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StringResponseDTO> deleteDisease(@PathVariable("name") String name) {
        diseaseService.deleteDiseaseByName(name);

        return new ResponseEntity<>(StringResponseDTO.builder().message("Successfully deleted the disease " + name + "!").build(), HttpStatus.OK);
    }
}
