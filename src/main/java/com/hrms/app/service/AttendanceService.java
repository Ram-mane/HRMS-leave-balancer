package com.hrms.app.service;

import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.responseDto.AttendanceResponseDto;
import com.hrms.app.entity.Attendance;
import org.springframework.stereotype.Service;

@Service
public interface AttendanceService {

    public AttendanceResponseDto markAttendance(AttendanceRequestDto attendanceRequestDto);
}
