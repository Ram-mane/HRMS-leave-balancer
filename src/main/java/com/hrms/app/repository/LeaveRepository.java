package com.hrms.app.repository;


import com.hrms.app.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, UUID> {

    @Query("SELECT l FROM Leave l WHERE l.leaveStatus = PENDING")
    List<Leave> getPendingLeaveRequest();

    @Query("SELECT l FROM Leave l WHERE l.leaveStatus = APPROVED")
    List<Leave> getApprovedLeaveRequest();

    Leave findByUniqueLeaveId(UUID uniqueLeaveId);
}
