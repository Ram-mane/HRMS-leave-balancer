package com.hrms.app.dto.responseDto;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.entity.Leave;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LeaveResponseDto {

     UUID uniqueLeaveId;
     LeaveStatus leaveStatus;
     String employeeEmail;
     String employeeName;
     LeaveType leaveType;
     String leaveReason;
     LocalDate leaveStartDate;
     LocalDate leaveEndDate;
     int leaveDuration;
     LocalDate appliedDate;

}
