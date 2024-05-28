package com.hrms.app.repository;


import com.hrms.app.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@Component
public interface EmpInfoRepository extends JpaRepository<Employee, UUID>{

    Employee findByEmpEmail(String empEmail);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.attendanceList WHERE e.attendanceMarked = false")
    List<Employee> getAbsentEmployeeList();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.attendanceList WHERE e.attendanceMarked = true")
    List<Employee> getPresentEmployeeList();
}
