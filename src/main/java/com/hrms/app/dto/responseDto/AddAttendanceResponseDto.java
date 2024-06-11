package com.hrms.app.dto.responseDto;

import com.hrms.app.Enum.AttendanceStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddAttendanceResponseDto {

    String empName;

    String empEmail;

    AttendanceStatus attendanceStatus;

    String message;
}
