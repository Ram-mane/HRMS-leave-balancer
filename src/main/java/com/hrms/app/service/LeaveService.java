package com.hrms.app.service;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.entity.Leave;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface LeaveService {

    LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto);

    PageResponseDto getPendingLeaveRequest(int pageNo) throws Exception;

    PageResponseDto getAllLeaveRequest(String empEmail, int pageNo, LeaveStatus leaveStatus) throws Exception;

    LeaveResponseDto getLeaveRequest(UUID uniqueLeaveId);

    PageResponseDto getApprovedLeaveRequest(int pageNo) throws Exception;

    LeaveResponseDto approveLeave(UUID uniqueLeaveId);

    LeaveResponseDto rejectLeave(UUID uniqueLeaveId);

    EmployeeLeaveResponseDto getNoOfLeavesLeft(String empEmail);
}
