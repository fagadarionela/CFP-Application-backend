package com.app.cfp.repository;

import com.app.cfp.entity.TherapeuticPlan;
import com.app.cfp.entity.TherapeuticPlanElement;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface TherapeuticPlanElementRepository extends JpaRepository<TherapeuticPlanElement, UUID> {
}
