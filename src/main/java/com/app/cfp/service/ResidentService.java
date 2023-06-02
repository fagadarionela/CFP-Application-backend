package com.app.cfp.service;

import com.app.cfp.entity.Resident;
import com.app.cfp.repository.ResidentRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ResidentService {

    private final ResidentRepository residentRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Optional<Resident> getResidentByUsername(String username) {
        return residentRepository.findByAccount_Username(username);
    }

    public Map<Resident, Long> getAllResidentsWithTheNumberOfCasesAllocatedIn(LocalDateTime today) {
        List<Resident> residents = residentRepository.findAll();

        Map<Resident, Long> residentsMap = new HashMap<>();

        residents.forEach(resident -> {
            long numberOfCases = resident.getMedicalCases().stream()
                    .filter(medicalCase -> medicalCase.getAllocationDate() != null && medicalCase.getAllocationDate().isAfter(today.withHour(0).withMinute(0).withSecond(0)) && medicalCase.getAllocationDate().isBefore(today.withHour(0).withMinute(0).withSecond(0).plusDays(1))).count();
            residentsMap.put(resident, numberOfCases);
        });

        return residentsMap;
    }

    public Map<Resident, Long> getAllResidentsWithTheNumberOfCasesOfASpecificDiagnosis(String diagnosis) {
        List<Resident> residents = residentRepository.findAll();

        Map<Resident, Long> residentsMap = new HashMap<>();

        residents.forEach(resident -> {
            long numberOfCases = resident.getMedicalCases().stream()
                    .filter(medicalCase -> diagnosis.equals(medicalCase.getPresumptiveDiagnosis())).count();
            residentsMap.put(resident, numberOfCases);
        });

        return residentsMap;
    }

    public Map<Resident, Long> getAllResidentsWithTheNumberOfCases() {
        List<Resident> residents = residentRepository.findAll();

        Map<Resident, Long> residentsMap = new HashMap<>();

        residents.forEach(resident -> residentsMap.put(resident, (long) resident.getMedicalCases().size()));

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

    public long getNumberOfMedicalCases(String username, String diagnosis) {
        Optional<Resident> residentOptional = residentRepository.findByAccount_Username(username);

        if (residentOptional.isPresent()) {
            return residentOptional.get().getMedicalCases().stream().filter(medicalCase -> medicalCase.getPresumptiveDiagnosis().equals(diagnosis)).count();
        }
        return 0L;
    }
}
