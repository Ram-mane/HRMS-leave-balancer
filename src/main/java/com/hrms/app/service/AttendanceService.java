package com.hrms.app.service;

import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.responseDto.AddAttendanceResponseDto;
import com.hrms.app.dto.responseDto.GetAttendanceResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {

    public AddAttendanceResponseDto markAttendance(AttendanceRequestDto attendanceRequestDto);

    List<GetAttendanceResponseDto> getAttendanceList(String empEmail);

    GetAttendanceResponseDto punchOut(String empEmail);

    List<GetAttendanceResponseDto> getAttendanceListLastMonth(String empEmail);
}
