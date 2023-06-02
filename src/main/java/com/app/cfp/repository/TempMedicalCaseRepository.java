package com.app.cfp.repository;

import com.app.cfp.entity.TempMedicalCase;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface TempMedicalCaseRepository extends JpaRepository<TempMedicalCase, UUID> {

}
