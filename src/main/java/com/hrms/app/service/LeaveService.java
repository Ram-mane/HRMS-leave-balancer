package com.hrms.app.service;


import com.hrms.app.entity.LeaveRequest;
import com.hrms.app.leaveDto.LeaveResponse;
import org.springframework.stereotype.Service;

@Service
public interface LeaveService {

    public LeaveRequest applyLeave(LeaveRequest leaveRequest);

    public LeaveResponse getPendingLeaveRequest();

    public LeaveResponse getApprovedLeaveRequest();

    public LeaveRequest aproveLeave(LeaveRequest leaveRequest, String empEmail);
}
