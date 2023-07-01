package com.app.cfp.controller;

import com.app.cfp.controller.handlers.exceptions.model.ResourceNotFoundException;
import com.app.cfp.dto.ResidentDTO;
import com.app.cfp.dto.StringResponseDTO;
import com.app.cfp.entity.Resident;
import com.app.cfp.mapper.ResidentMapper;
import com.app.cfp.service.ResidentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/residents")
@AllArgsConstructor
@CrossOrigin
public class ResidentController {

    private final ResidentService residentService;

    private final ResidentMapper residentMapper;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StringResponseDTO> addResident(@RequestBody ResidentDTO residentDTO) {
        Resident resident = residentService.addResident(residentMapper.toDomain(residentDTO));

        if (resident != null) {
            return new ResponseEntity<>(StringResponseDTO.builder().message("The resident with id " + resident.getId() + " was created!").build(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(StringResponseDTO.builder().message("Can not add resident!").build(), HttpStatus.CONFLICT);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ResidentDTO>> getAllResidents() {
        List<Resident> residents = residentService.getAllResidents();

        return new ResponseEntity<>(residents.stream().map(residentMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<Double> getGradeOfResident(Principal principal) {
        Double grade = residentService.getResidentGradeByUsername(principal.getName());
        if (grade == null) {
            throw new ResourceNotFoundException("Residentul nu a putut fi gasit!");
        }

        return new ResponseEntity<>(grade, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StringResponseDTO> deleteResident(@PathVariable("id") UUID id) {
        residentService.deleteResidentById(id);

        return new ResponseEntity<>(StringResponseDTO.builder().message("Successfully deleted the resident!").build(), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StringResponseDTO> deleteAllResidents() {
        residentService.deleteAllResidents();
        return new ResponseEntity<>(StringResponseDTO.builder().message("Successfully deleted all the residents!").build(), HttpStatus.OK);
    }

    @GetMapping("/numberOfMedicalCases/{username}/{diagnosis}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Long> getNumberOfMedicalCases(@PathVariable("username") String username, @PathVariable("diagnosis") String diagnosis) {
        return new ResponseEntity<>(residentService.getNumberOfMedicalCases(username, diagnosis), HttpStatus.OK);
    }
    @GetMapping("/gradeOfMedicalCases/{username}/{diagnosis}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Double> getGradeOfMedicalCases(@PathVariable("username") String username, @PathVariable("diagnosis") String diagnosis) {
        return new ResponseEntity<>(residentService.getGradeOfMedicalCases(username, diagnosis), HttpStatus.OK);
    }
}
