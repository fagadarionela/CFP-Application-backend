package com.app.cfp.service;

import com.app.cfp.entity.TempMedicalCase;
import com.app.cfp.repository.TempMedicalCaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SystemService {

    private final TempMedicalCaseRepository tempMedicalCaseRepository;

    public List<TempMedicalCase> getAllTempMedicalCases() {
        return tempMedicalCaseRepository.findAll();
    }

    public TempMedicalCase addTempMedicalCase(TempMedicalCase tempMedicalCase) {
        return tempMedicalCaseRepository.save(tempMedicalCase);
    }

    public void deleteTempMedicalCase(UUID id) {
        tempMedicalCaseRepository.deleteById(id);
    }
}