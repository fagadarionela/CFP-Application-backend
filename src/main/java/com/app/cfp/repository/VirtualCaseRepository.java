package com.app.cfp.repository;

import com.app.cfp.entity.VirtualCase;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface VirtualCaseRepository extends JpaRepository<VirtualCase, UUID> {

    List<VirtualCase> findAllByPresumptiveDiagnosis(String presumtiveDiagnosis);
}
