package com.app.cfp.repository;

import com.app.cfp.entity.Resident;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface ResidentRepository extends JpaRepository<Resident, UUID> {

    Optional<Resident> findByAccount_Username(String username);

    @Query(nativeQuery=true, value="SELECT r.grade  FROM resident r WHERE r.username = :username")
    Double findGradeByAccount_Username(String username);
}
