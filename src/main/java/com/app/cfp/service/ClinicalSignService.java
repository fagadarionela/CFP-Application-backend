package com.app.cfp.service;

import com.app.cfp.entity.ClinicalSign;
import com.app.cfp.repository.ClinicalSignRepository;
import com.app.cfp.repository.DiseaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClinicalSignService {
    private final ClinicalSignRepository clinicalSignRepository;
    public List<ClinicalSign> getAllClinicalSigns(){
        return clinicalSignRepository.findAll();
    }
}
