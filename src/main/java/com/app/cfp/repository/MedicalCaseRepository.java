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
    Page<MedicalCase> findAllByCompletedTrue(Pageable pageable);

    Set<MedicalCase> findAllByEncodedInfoOrderByInsertDate(String encodedInfo);

    Page<MedicalCase> findAllByResident_Account_UsernameAndCompletedFalseAndEncodedInfoContains(String username, Pageable pageable, String encodedInfo);

    Page<MedicalCase> findAllByResident_Account_UsernameAndCompletedTrueAndResidentDiagnosisContainsIgnoreCase(String username, Pageable pageable, String diagnostic);
}
