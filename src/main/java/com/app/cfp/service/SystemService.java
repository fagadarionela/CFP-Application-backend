package com.app.cfp.service;

import com.app.cfp.entity.TempMedicalCase;
import com.app.cfp.entity.VirtualCase;
import com.app.cfp.repository.TempMedicalCaseRepository;
import com.app.cfp.repository.VirtualCaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SystemService {

    private final TempMedicalCaseRepository tempMedicalCaseRepository;

    private final VirtualCaseRepository virtualCaseRepository;

    public TempMedicalCase findRandomTempMedicalCase() {
        return tempMedicalCaseRepository.findRandomTempMedicalCase();
    }

    public TempMedicalCase addTempMedicalCase(TempMedicalCase tempMedicalCase) {
        return tempMedicalCaseRepository.save(tempMedicalCase);
    }

    public VirtualCase addVirtualCase(VirtualCase virtualCase) {
        return virtualCaseRepository.save(virtualCase);
    }

    public void deleteTempMedicalCase(UUID id) {
        tempMedicalCaseRepository.deleteById(id);
    }

    public void deleteAllTempMedicalCases() {
        tempMedicalCaseRepository.deleteAll();
    }

    public void deleteAllVirtualCases() {
        virtualCaseRepository.deleteAll();
    }

    public long countAllTempMedicalCases(){return tempMedicalCaseRepository.count();}

    public long countAllVirtualCases(){return virtualCaseRepository.count();}

}