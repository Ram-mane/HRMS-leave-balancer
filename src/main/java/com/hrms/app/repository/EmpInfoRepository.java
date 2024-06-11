package com.hrms.app.repository;


import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface EmpInfoRepository extends JpaRepository<Employee, UUID>{

    Employee findByEmpEmail(String empEmail);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.attendanceList WHERE e.attendanceMarked = false")
    List<Employee> getAbsentEmployeeList();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.attendanceList WHERE e.attendanceMarked = true")
    List<Employee> getPresentEmployeeList();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.attendanceList WHERE e.empEmail = :empEmail")
    Employee getEmployeeAndAttendanceListByEmpEmail(@Param("empEmail") String empEmail);

    @Query("SELECT e.attendanceList FROM Employee e WHERE e.empEmail = :empEmail")
    List<Attendance> getAttendanceListByEmpEmail(@Param("empEmail") String empEmail);

    @Modifying
    @Query("UPDATE Employee e SET e.attendanceMarked = false WHERE e.organization.organizationCode = :code AND e.shiftNumber = :shift")
    void updateAttendanceMarkedForOrganizationAndShift(UUID code, Integer shift);
}
