package com.hrms.app.dto.responseDto;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
     Double leaveDuration;
     LocalDate appliedDate;

}
