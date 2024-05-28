package com.hrms.app.mapper;

import com.hrms.app.Enum.AttendanceStatus;
import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.responseDto.AddAttendanceResponseDto;
import com.hrms.app.dto.responseDto.GetAttendanceResponseDto;
import com.hrms.app.entity.Attendance;

public class AttendanceMapper {

    public static Attendance AttendanceRequestDtoToAttendance(AttendanceRequestDto attendanceRequestDto) {
        return Attendance.builder()
                         .attendanceStatus(attendanceRequestDto.getAttendanceStatus())
                         .build();
    }

    public static AddAttendanceResponseDto AttendanceToAddAttendanceResponseDto(Attendance attendance) {
        AddAttendanceResponseDto addAttendanceResponseDto = AddAttendanceResponseDto.builder()
                                                    .attendanceStatus(attendance.getAttendanceStatus())
                                                    .empName(attendance.getEmployee().getEmpName())
                                                    .empEmail(attendance.getEmployee().getEmpEmail())
                                                    .build();

        if(attendance.getAttendanceStatus().equals(AttendanceStatus.PRESENT))
            addAttendanceResponseDto.setMessage("Welcome " + attendance.getEmployee().getEmpName());
        else {
            addAttendanceResponseDto.setMessage(attendance.getEmployee().getEmpName()+", you are marked" +
                    " to be on leave today");
        }

        return addAttendanceResponseDto;
    }

    public static GetAttendanceResponseDto AttendanceToGetAttendanceResponseDto(Attendance attendance) {
        GetAttendanceResponseDto getAttendanceResponseDto = GetAttendanceResponseDto.builder()
                                                            .attendanceStatus(attendance.getAttendanceStatus())
                                                            .empName(attendance.getEmployee().getEmpName())
                                                            .empEmail(attendance.getEmployee().getEmpEmail())
                                                            .punchInTime(attendance.getPunchInTime())
                                                            .activeTime(attendance.getActiveTime())
                                                            .date(attendance.getDate())
                                                            .build();

        if(attendance.getPunchOutTime() != null) {
            getAttendanceResponseDto.setPunchOutTime(attendance.getPunchOutTime());
        }

        return getAttendanceResponseDto;
    }
}
