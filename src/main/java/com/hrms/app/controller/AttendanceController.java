package com.hrms.app.controller;

import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.responseDto.AddAttendanceResponseDto;
import com.hrms.app.dto.responseDto.GetAttendanceResponseDto;
import com.hrms.app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/markAttendance")
    public ResponseEntity markAttendance(@RequestBody AttendanceRequestDto attendanceRequestDto) {
        try{
            AddAttendanceResponseDto addAttendanceResponseDto = attendanceService.markAttendance(attendanceRequestDto);
            return new ResponseEntity<>(addAttendanceResponseDto, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAttendanceList")
    public ResponseEntity getAttendanceList(@RequestParam String empEmail) {
        try{
            List<GetAttendanceResponseDto> getAttendanceResponseDtoList = attendanceService.getAttendanceList(empEmail);
            return new ResponseEntity<>(getAttendanceResponseDtoList, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/punchOut")
    public ResponseEntity punchOut(@RequestParam String empEmail) {
        try{
            GetAttendanceResponseDto getAttendanceResponseDto = attendanceService.punchOut(empEmail);
            return new ResponseEntity<>(getAttendanceResponseDto, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_attendance_list_last_month")
    public ResponseEntity getAttendanceListLastMonth(@RequestParam String empEmail) {
        try{
            List<GetAttendanceResponseDto> getAttendanceResponseDtoList = attendanceService.getAttendanceListLastMonth(empEmail);
            return new ResponseEntity<>(getAttendanceResponseDtoList, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
