package com.hrms.app.controller;

import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.responseDto.AttendanceResponseDto;
import com.hrms.app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/markAttendance")
    public ResponseEntity markAttendance(@RequestBody AttendanceRequestDto attendanceRequestDto) {
        try{
            AttendanceResponseDto attendanceResponseDto = attendanceService.markAttendance(attendanceRequestDto);
            return new ResponseEntity<>(attendanceResponseDto, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
