package com.hrms.app.repository;


import com.hrms.app.entity.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpInfoRepository extends JpaRepository<EmployeeInfo, UUID>{

    EmployeeInfo findByEmpEmail(String empEmail);
}
