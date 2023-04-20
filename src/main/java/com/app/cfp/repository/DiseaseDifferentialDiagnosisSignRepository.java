package com.app.cfp.repository;

import com.app.cfp.entity.DifferentialDiagnosis;
import com.app.cfp.entity.DifferentialDiagnosisSign;
import com.app.cfp.entity.DiseaseDifferentialDiagnosisSign;
import com.app.cfp.entity.Sign;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface DiseaseDifferentialDiagnosisSignRepository extends JpaRepository<DiseaseDifferentialDiagnosisSign, UUID> {

}
