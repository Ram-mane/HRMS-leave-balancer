package com.hrms.app.repository;


import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, UUID> {

    @Query("SELECT l FROM Leave l WHERE l.leaveStatus = PENDING")
    Page<Leave> getPendingLeaveRequest(Pageable pageable);

    @Query("SELECT l FROM Leave l WHERE l.leaveStatus = APPROVED")
    Page<Leave> getApprovedLeaveRequest(Pageable pageable);

    Leave findByUniqueLeaveId(UUID uniqueLeaveId);

    @Query("SELECT l FROM Leave l WHERE l.employee.empEmail = :empEmail AND l.appliedDate = :date")
    Optional<Leave> findByEmployeeEmailAndDate(String empEmail, LocalDate date);
}
