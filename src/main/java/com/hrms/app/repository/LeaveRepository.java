package com.hrms.app.repository;


import com.hrms.app.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, UUID> {

    @Query("SELECT l FROM LeaveRequest l WHERE l.leaveStatus = 'Pending'")
    List<LeaveRequest> getPendingLeaveRequest();

    @Query("SELECT l FROM LeaveRequest l WHERE l.leaveStatus = 'Approved'")
    List<LeaveRequest> getApprovedLeaveRequest();

    LeaveRequest findByEmpEmail(String empEmail);
}
