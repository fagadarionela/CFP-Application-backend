package com.app.cfp.service;

import com.app.cfp.entity.Resident;
import com.app.cfp.repository.ResidentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResidentService {
    private final ResidentRepository residentRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Map<Resident, Long> getAllResidentsThatDoNotHaveAnAllocatedCaseIn(LocalDateTime today) {

        List<Resident> residents = residentRepository.findAll();

        Map<Resident, Long> residentsMap = new HashMap<>();

        residents.forEach(resident -> {
            long numberOfCases = resident.getMedicalCases().stream()
                    .filter(medicalCase -> medicalCase.getAllocationDate() != null && medicalCase.getAllocationDate().isAfter(today) && medicalCase.getAllocationDate().isBefore(today.plusDays(1))).count();
            residentsMap.put(resident, numberOfCases);
        });

        System.out.println(residentsMap);

        return residentsMap;
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
