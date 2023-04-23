package com.app.cfp.repository;

import com.app.cfp.entity.MedicalCase;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@Transactional
public interface MedicalCaseRepository extends JpaRepository<MedicalCase, UUID> {
    Page<MedicalCase> findAllByCompletedByResidentTrueAndCompletedByExpertFalseAndEncodedInfoContainsOrderByInsertDateDesc(Pageable pageable, String encodedInfo);

    Set<MedicalCase> findAllByEncodedInfoOrderByInsertDateDesc(String encodedInfo);

    Page<MedicalCase> findAllByResident_Account_UsernameAndCompletedByResidentFalseAndEncodedInfoContainsOrderByInsertDateDesc(String username, Pageable pageable, String encodedInfo);

    Page<MedicalCase> findAllByResident_Account_UsernameAndCompletedByResidentTrueAndResidentDiagnosisContainsIgnoreCaseOrderByInsertDateDesc(String username, Pageable pageable, String diagnostic);
}
