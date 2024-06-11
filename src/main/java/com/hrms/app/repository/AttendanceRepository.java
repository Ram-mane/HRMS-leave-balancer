package com.hrms.app.repository;

import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    @Query("SELECT a FROM Attendance a WHERE a.employee.empEmail = :empEmail AND a.date = :date")
    Optional<Attendance> findByEmployeeEmailAndDate(String empEmail, LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.employee.empEmail = :empEmail AND a.date >= :date")
    List<Attendance> findAttentionAfterDate(String empEmail, LocalDate date);

}
