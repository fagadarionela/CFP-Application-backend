package com.app.cfp.repository;

import com.app.cfp.entity.DifferentialDiagnosis;
import com.app.cfp.entity.DifferentialDiagnosisSign;
import com.app.cfp.entity.DifferentialDiagnosisSignKey;
import com.app.cfp.entity.Sign;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface DifferentialDiagnosisSignRepository extends JpaRepository<DifferentialDiagnosisSign, DifferentialDiagnosisSignKey> {

    DifferentialDiagnosisSign findByDifferentialDiagnosisAndSign(DifferentialDiagnosis differentialDiagnosis, Sign sign);
}
