package com.app.cfp.repository;

import com.app.cfp.entity.MyDate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface MyDateRepository extends JpaRepository<MyDate, String> {

}
