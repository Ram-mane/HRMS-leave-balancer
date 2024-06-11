package com.hrms.app.controller;

import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.requestDto.ChangeAttendanceRequestDto;
import com.hrms.app.dto.requestDto.GetAttendanceRequestDto;
import com.hrms.app.dto.responseDto.*;
import com.hrms.app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
            ResponseDtoWrapper<AddAttendanceResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", addAttendanceResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAttendanceList")
    public ResponseEntity getAttendanceList(@RequestParam GetAttendanceRequestDto getAttendanceRequestDto, @RequestParam UUID organizationCode) {
        try{
            PageResponseDto pageResponseDto = attendanceService.getAttendanceList(getAttendanceRequestDto, organizationCode);
            ResponseDtoWrapper<PageResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", pageResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAttendanceListOfADay/{pageNo}")
    public ResponseEntity getAttendanceListOfADay(@PathVariable int pageNo, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date, @RequestParam UUID organizationCode) {
        try{
            PageResponseDto pageResponseDto = attendanceService.getAttendanceListOfADay(pageNo, date, organizationCode);
            ResponseDtoWrapper<PageResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", pageResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/punchOut")
    public ResponseEntity punchOut(@RequestParam String empEmail) {
        try{
            GetAttendanceResponseDto getAttendanceResponseDto = attendanceService.punchOut(empEmail);
            ResponseDtoWrapper<GetAttendanceResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", getAttendanceResponseDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/attendanceCorrection")
    public ResponseEntity attendanceCorrection(@RequestBody ChangeAttendanceRequestDto changeAttendanceRequestDto) {
        try{
            GetAttendanceResponseDto getAttendanceResponseDto = attendanceService.attendanceCorrection(changeAttendanceRequestDto);
            ResponseDtoWrapper<GetAttendanceResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", getAttendanceResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
