package com.app.cfp.controller;

import com.app.cfp.dto.ResidentDTO;
import com.app.cfp.entity.Resident;
import com.app.cfp.mapper.ResidentsMapper;
import com.app.cfp.service.ResidentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/residents")
@AllArgsConstructor
@CrossOrigin
public class ResidentController {

    private final ResidentService residentService;

    private final ResidentsMapper residentsMapper;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<String> addResident(@RequestBody ResidentDTO residentDTO) {
        Resident resident = residentService.addResident(residentsMapper.toDomain(residentDTO));

        if (resident != null) {
            return new ResponseEntity<>("The resident with id " + resident.getId() + " was created!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Can not add resident!", HttpStatus.CONFLICT);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<String> deleteAllResidents() {
        residentService.deleteAllResidents();
        return new ResponseEntity<>("Successfully deleted all the residents!", HttpStatus.OK);
    }
}
