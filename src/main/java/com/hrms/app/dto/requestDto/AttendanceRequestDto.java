package com.hrms.app.dto.requestDto;

import com.hrms.app.Enum.AttendanceStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AttendanceRequestDto {

    String empEmail;

    AttendanceStatus attendanceStatus;

}
