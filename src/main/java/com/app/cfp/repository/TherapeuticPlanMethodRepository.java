package com.app.cfp.repository;

import com.app.cfp.entity.Method;
import com.app.cfp.entity.TherapeuticPlan;
import com.app.cfp.entity.TherapeuticPlanMethod;
import com.app.cfp.entity.TherapeuticPlanMethodKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TherapeuticPlanMethodRepository extends JpaRepository<TherapeuticPlanMethod, TherapeuticPlanMethodKey> {

    TherapeuticPlanMethod findByTherapeuticPlanAndMethod(TherapeuticPlan therapeuticPlan, Method method);
}
