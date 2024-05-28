package com.hrms.app.dto.requestDto;

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
public class LeaveRequestDto {

    String employeeEmail;
    LeaveType leaveType;
    String leaveReason;
    double leaveDuration;
    LocalDate leaveStartDate;
    LocalDate leaveEndDate;


}
