package com.app.cfp.repository;

import com.app.cfp.entity.TempMedicalCase;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface TempMedicalCaseRepository extends JpaRepository<TempMedicalCase, UUID> {

    @Query(nativeQuery=true, value="SELECT *  FROM temp_medical_case ORDER BY random() LIMIT 1")
    TempMedicalCase findRandomTempMedicalCase();
}
