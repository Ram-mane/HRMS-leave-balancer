package com.hrms.app.service;


import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.entity.Leave;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface LeaveService {

    LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto);

    List<LeaveResponseDto> getPendingLeaveRequest();

    List<LeaveResponseDto> getAllLeaveRequest(String empEmail);

    LeaveResponseDto getLeaveRequest(UUID uniqueLeaveId);

    List<LeaveResponseDto> getApprovedLeaveRequest();

    LeaveResponseDto approveLeave(UUID uniqueLeaveId);

    LeaveResponseDto rejectLeave(UUID uniqueLeaveId);
}
