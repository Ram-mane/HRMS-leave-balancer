package com.hrms.app.service;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface LeaveService {

    LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto);

    PageResponseDto getAllLeaveRequest(int pageNo, LeaveStatus leaveStatus, UUID organizationCode) throws Exception;

    PageResponseDto getAllLeaveRequestOfEmp(String empEmail, int pageNo, LeaveStatus leaveStatus, UUID organizationCode) throws Exception;

    LeaveResponseDto getLeaveRequest(UUID uniqueLeaveId);

    LeaveResponseDto approveOrRejectLeave(UUID uniqueLeaveId, LeaveStatus leaveStatus);

    EmployeeLeaveResponseDto getNoOfLeavesLeft(String empEmail, UUID organizationCode);

    LeaveResponseDto updateLeave(LeaveRequestDto leaveRequestDto, UUID uniqueLeaveId);

    String cancelLeaveRequest(String password, UUID uniqueLeaveId);
}
