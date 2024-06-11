package com.hrms.app.service;

import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.requestDto.ChangeAttendanceRequestDto;
import com.hrms.app.dto.requestDto.GetAttendanceRequestDto;
import com.hrms.app.dto.responseDto.AddAttendanceResponseDto;
import com.hrms.app.dto.responseDto.GetAttendanceResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public interface AttendanceService {

    public AddAttendanceResponseDto markAttendance(AttendanceRequestDto attendanceRequestDto);

    PageResponseDto getAttendanceList(GetAttendanceRequestDto getAttendanceRequestDto, UUID organizationCode) throws Exception;

    PageResponseDto getAttendanceListOfADay(int pageNo, LocalDate date, UUID organizationCode) throws Exception;

    GetAttendanceResponseDto punchOut(String empEmail);

    GetAttendanceResponseDto attendanceCorrection(ChangeAttendanceRequestDto changeAttendanceRequestDto);
}
