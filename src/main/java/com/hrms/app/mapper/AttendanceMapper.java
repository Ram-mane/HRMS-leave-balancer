package com.hrms.app.mapper;

import com.hrms.app.Enum.AttendanceStatus;
import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.responseDto.AttendanceResponseDto;
import com.hrms.app.entity.Attendance;

public class AttendanceMapper {

    public static Attendance AttendanceRequestDtoToAttendance(AttendanceRequestDto attendanceRequestDto) {
        return Attendance.builder()
                         .attendanceStatus(attendanceRequestDto.getAttendanceStatus())
                         .build();
    }

    public static AttendanceResponseDto AttendanceToAttendanceResponseDto(Attendance attendance) {
        AttendanceResponseDto attendanceResponseDto = AttendanceResponseDto.builder()
                                                    .attendanceStatus(attendance.getAttendanceStatus())
                                                    .empName(attendance.getEmployee().getEmpName())
                                                    .empEmail(attendance.getEmployee().getEmpEmail())
                                                    .build();

        if(attendance.getAttendanceStatus().equals(AttendanceStatus.PRESENT))
            attendanceResponseDto.setMessage("Welcome " + attendance.getEmployee().getEmpName());
        else {
            attendanceResponseDto.setMessage(attendance.getEmployee().getEmpName()+", you are marked" +
                    " to be on leave today");
        }

        return attendanceResponseDto;
    }
}
