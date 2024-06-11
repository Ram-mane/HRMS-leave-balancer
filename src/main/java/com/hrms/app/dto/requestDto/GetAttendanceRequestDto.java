package com.hrms.app.dto.requestDto;

import com.hrms.app.Enum.AttendanceFrom;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetAttendanceRequestDto {

    String empEmail;

    AttendanceFrom attendanceFrom;

    LocalDate fromDate;

    LocalDate tillDate;

    Integer pageNo;
}
