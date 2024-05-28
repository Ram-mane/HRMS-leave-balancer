package com.hrms.app.controller;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;




@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/leaves")
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
    public ResponseEntity getPendingLeaveRequest(@RequestParam int pageNo) {
        try{
            PageResponseDto pageResponseDto =  leaveService.getPendingLeaveRequest(pageNo);
            return new ResponseEntity<>(pageResponseDto, HttpStatus.OK);
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
    public ResponseEntity getAllLeaveRequest(@RequestParam String empEmail, @RequestParam int pageNo, @RequestParam LeaveStatus leaveStatus) {
        try{
            PageResponseDto pageResponseDto =  leaveService.getAllLeaveRequest(empEmail, pageNo, leaveStatus);
            return new ResponseEntity<>(pageResponseDto, HttpStatus.OK);
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
    public ResponseEntity getApprovedLeaveRequest(@RequestParam int pageNo) {
        try{
            PageResponseDto pageResponseDto =  leaveService.getApprovedLeaveRequest(pageNo);
            return new ResponseEntity<>(pageResponseDto, HttpStatus.OK);
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

    @GetMapping("/getNoOfLeavesLeft")
    public ResponseEntity getNoOfLeavesLeft(@RequestParam String empEmail) {
        try{
            EmployeeLeaveResponseDto employeeLeaveResponseDto  = leaveService.getNoOfLeavesLeft(empEmail);
            return new ResponseEntity(employeeLeaveResponseDto, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
