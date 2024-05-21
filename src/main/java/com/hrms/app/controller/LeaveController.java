package com.hrms.app.controller;


import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.entity.Leave;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;




@RestController
@RequestMapping("/Leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/applyLeave")
    public ResponseEntity applyLeave(@RequestBody LeaveRequestDto leaveRequestDto) {

        try{
            LeaveResponseDto leaveResponseDto = leaveService.applyLeave(leaveRequestDto);
            return new ResponseEntity<>(leaveResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPendingLeaveRequest")
    public ResponseEntity getPendingLeaveRequest() {
        try{
            List<LeaveResponseDto> leaveResponseDtoList =  leaveService.getPendingLeaveRequest();
            return new ResponseEntity<>(leaveResponseDtoList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approveLeave")
    public ResponseEntity approveLeave(@RequestParam UUID uniqueLeaveId) {

        try{
            LeaveResponseDto leaveResponseDto = leaveService.approveLeave(uniqueLeaveId);
            return new ResponseEntity(leaveResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllLeaveRequestOfEmployee")
    public ResponseEntity getAllLeaveRequest(@RequestParam String empEmail) {
        try{
            List<LeaveResponseDto> leaveResponseDtoList =  leaveService.getAllLeaveRequest(empEmail);
            return new ResponseEntity<>(leaveResponseDtoList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getLeaveRequest")
    public ResponseEntity getLeaveRequest(@RequestParam UUID uniqueLeaveId) {
        try{
            LeaveResponseDto leaveResponseDto =  leaveService.getLeaveRequest(uniqueLeaveId);
            return new ResponseEntity<>(leaveResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getApprovedLeaveRequest")
    public ResponseEntity getApprovedLeaveRequest() {
        try{
            List<LeaveResponseDto> leaveResponseDtoList =  leaveService.getApprovedLeaveRequest();
            return new ResponseEntity<>(leaveResponseDtoList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rejectLeaveRequest")
    public ResponseEntity rejectLeave(@RequestParam UUID uniqueLeaveId) {
        try{
            LeaveResponseDto leaveResponseDto =  leaveService.rejectLeave(uniqueLeaveId);
            return new ResponseEntity<>(leaveResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
