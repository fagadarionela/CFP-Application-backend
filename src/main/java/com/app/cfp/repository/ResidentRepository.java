package com.app.cfp.repository;

import com.app.cfp.entity.MedicalCase;
import com.app.cfp.entity.Resident;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface ResidentRepository extends JpaRepository<Resident, UUID> {
    Optional<Resident> findByAccount_Username(String username);
}
