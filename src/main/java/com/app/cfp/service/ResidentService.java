package com.app.cfp.service;

import com.app.cfp.entity.Resident;
import com.app.cfp.repository.ResidentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResidentService {
    private final ResidentRepository residentRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Resident addResident(Resident resident) {
        resident.getAccount().setPassword(bCryptPasswordEncoder.encode(resident.getAccount().getPassword()));
        return residentRepository.save(resident);
    }

    public void deleteResidentById(UUID id) {
        residentRepository.deleteById(id);
    }

    public void deleteAllResidents() {
        residentRepository.deleteAll();
    }
}
