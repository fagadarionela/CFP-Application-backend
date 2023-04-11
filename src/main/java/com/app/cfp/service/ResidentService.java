package com.app.cfp.service;

import com.app.cfp.controller.handlers.exceptions.model.ResourceNotFoundException;
import com.app.cfp.entity.Resident;
import com.app.cfp.repository.ResidentRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResidentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResidentService.class);
    private final ResidentRepository residentRepository;

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Resident findResidentByUsername(String username) {
        Optional<Resident> residentOptional = residentRepository.findByAccount_Username(username);
        if (!residentOptional.isPresent()) {
            LOGGER.error("Resident with username {} was not found in db", username);
            throw new ResourceNotFoundException(Resident.class.getSimpleName() + " with username: " + username);
        }
        return residentOptional.get();
    }

    public Resident addResident(Resident resident) {
        return residentRepository.save(resident);
    }

    public void deleteAllResidents() {
        residentRepository.deleteAll();
    }
}
