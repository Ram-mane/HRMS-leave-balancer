package com.hrms.app.dto.requestDto;

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
public class UpdatePolicyRequestDto {

    UUID policyCode;

    Boolean punchOutAllowed;

    Boolean multipleShiftAllowed;

    List<List<LocalTime>> shiftTiming;

    Boolean attendanceByManager;

    Integer weekendLength;

    Integer monthDurationInDays; // 26 or 30

    Boolean adjustCompensationWorkThroughSalary; // True- add in salary, False- adjust in casual leaves

    Integer monthStartDay;
}
