package com.app.cfp.repository;

import com.app.cfp.entity.TherapeuticPlanGrade;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface TherapeuticPlanGradeRepository extends JpaRepository<TherapeuticPlanGrade, UUID> {

}
