package com.hrms.app.repository;

import com.hrms.app.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, UUID> {

    Designation findByDesignation(String designation);

}
