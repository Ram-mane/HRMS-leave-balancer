package com.hrms.app.dto.requestDto;

import com.hrms.app.Enum.AttendanceStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChangeAttendanceRequestDto {

    String empEmail;

    AttendanceStatus attendanceStatus;

    LocalDate date;

    LocalTime punchInTime;

    LocalTime punchOutTime;

    Double activeTime;
}
