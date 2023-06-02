package com.app.cfp.repository;

import com.app.cfp.entity.Disease;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface DiseaseRepository extends JpaRepository<Disease, UUID> {

    Disease findByName(String name);

    List<Disease> findAllByRetinalConditionIsTrue();

    void deleteByName(String name);
}
