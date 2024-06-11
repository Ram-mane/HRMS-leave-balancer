package com.hrms.app.repository;

import com.hrms.app.Enum.AttendanceStatus;
import com.hrms.app.entity.Attendance;
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

    List<Attendance> findByDate(LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.employee.empEmail = :empEmail AND a.date >= :date")
    List<Attendance> findAttentionAfterDate(String empEmail, LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.employee.empEmail = :empEmail AND a.date >= :fromDate AND a.date <= :toDate")
    List<Attendance> findAttentionBetweenDate(String empEmail, LocalDate fromDate, LocalDate toDate);

    @Query("SELECT a FROM Attendance a WHERE a.employee.empEmail = :empEmail AND a.date >= :fromDate AND a.date <= :toDate AND a.attendanceStatus = :status")
    List<Attendance> findAttentionBetweenDateAndStatus(String empEmail, LocalDate fromDate, LocalDate toDate, AttendanceStatus status);

}
