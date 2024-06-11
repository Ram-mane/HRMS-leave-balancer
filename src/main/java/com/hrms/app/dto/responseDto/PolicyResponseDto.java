package com.hrms.app.dto.responseDto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PolicyResponseDto {

    UUID policyCode;

    UUID organizationCode;

    Boolean punchOutAllowed;

    Boolean multipleShiftAllowed;

    List<List<LocalTime>> shiftTiming;

    Boolean attendanceByManager;

    Integer weekendLength;

    Integer monthDurationInDays;

    Boolean adjustCompensationWorkThroughSalary;

    Integer monthStartDay;
}
