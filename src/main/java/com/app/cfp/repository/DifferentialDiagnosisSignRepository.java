package com.app.cfp.repository;

import com.app.cfp.entity.DifferentialDiagnosisSign;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface DifferentialDiagnosisSignRepository extends JpaRepository<DifferentialDiagnosisSign, UUID> {

}
