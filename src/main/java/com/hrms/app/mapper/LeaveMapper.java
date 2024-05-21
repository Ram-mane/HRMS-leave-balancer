package com.hrms.app.mapper;

import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.entity.Leave;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.hrms.app.Enum.LeaveStatus.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class LeaveMapper {

    public static Leave leaveRequestDtoToLeave(LeaveRequestDto leaveRequestDto) {

        Leave leave =  Leave.builder()
                        .leaveType(leaveRequestDto.getLeaveType())
                        .leaveReason(leaveRequestDto.getLeaveReason())
                        .leaveStartDate(leaveRequestDto.getLeaveStartDate())
                        .leaveEndDate(leaveRequestDto.getLeaveEndDate())
                        .build();

        leave.setLeaveStatus(PENDING);
        leave.setUniqueLeaveId(UUID.randomUUID());

        long days = DAYS.between(leaveRequestDto.getLeaveStartDate(), leaveRequestDto.getLeaveEndDate());
        leave.setLeaveDuration((int)days + 1);

        leave.setCreatedAt(LocalDateTime.now());
        leave.setCreatedBy("Admin");
        leave.setModifiedAt(LocalDateTime.now());
        leave.setModifiedBy("Admin");

        return leave;
    }

    public static LeaveResponseDto leaveToLeaveResponseDto(Leave leave) {

        return LeaveResponseDto.builder()
                                .uniqueLeaveId(leave.getUniqueLeaveId())
                                .employeeEmail(leave.getEmployee().getEmpEmail())
                                .leaveType(leave.getLeaveType())
                                .leaveReason(leave.getLeaveReason())
                                .appliedDate(leave.getAppliedDate())
                                .leaveStartDate(leave.getLeaveStartDate())
                                .leaveEndDate(leave.getLeaveEndDate())
                                .leaveDuration(leave.getLeaveDuration())
                                .employeeName(leave.getEmployee().getEmpName())
                                .leaveStatus(leave.getLeaveStatus())
                                .build();

    }
}
