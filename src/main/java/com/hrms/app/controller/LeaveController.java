package com.hrms.app.controller;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.*;
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
            ResponseDtoWrapper<LeaveResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", leaveResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllLeaveRequestBasedOnStatus/{pageNo}")
    public ResponseEntity getAllLeaveRequest(@PathVariable int pageNo, @RequestParam LeaveStatus leaveStatus, @RequestParam UUID organizationCode) {
        try{
            PageResponseDto pageResponseDto =  leaveService.getAllLeaveRequest(pageNo, leaveStatus, organizationCode);
            ResponseDtoWrapper<PageResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", pageResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approveOrRejectLeave")
    public ResponseEntity approveOrRejectLeave(@RequestParam UUID uniqueLeaveId, @RequestParam LeaveStatus leaveStatus) {

        try{
            LeaveResponseDto leaveResponseDto = leaveService.approveOrRejectLeave(uniqueLeaveId, leaveStatus);
            ResponseDtoWrapper<LeaveResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", leaveResponseDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllLeaveRequestOfEmployee/{pageNo}")
    public ResponseEntity getAllLeaveRequestOfEmp(@PathVariable int pageNo, @RequestParam String empEmail, @RequestParam LeaveStatus leaveStatus, @RequestParam UUID organizationCode) {
        try{
            PageResponseDto pageResponseDto =  leaveService.getAllLeaveRequestOfEmp(empEmail, pageNo, leaveStatus, organizationCode);
            ResponseDtoWrapper<PageResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", pageResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getLeaveRequest")
    public ResponseEntity getLeaveRequest(@RequestParam UUID uniqueLeaveId) {
        try{
            LeaveResponseDto leaveResponseDto =  leaveService.getLeaveRequest(uniqueLeaveId);
            ResponseDtoWrapper<LeaveResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", leaveResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getNoOfLeavesLeft")
    public ResponseEntity getNoOfLeavesLeft(@RequestParam String empEmail, @RequestParam UUID organizationCode) {
        try{
            EmployeeLeaveResponseDto employeeLeaveResponseDto  = leaveService.getNoOfLeavesLeft(empEmail, organizationCode);
            ResponseDtoWrapper<EmployeeLeaveResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", employeeLeaveResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changeLeaveRequest")
    public ResponseEntity updateLeave(@RequestBody LeaveRequestDto leaveRequestDto, @RequestParam UUID uniqueLeaveId) {

        try{
            LeaveResponseDto leaveResponseDto = leaveService.updateLeave(leaveRequestDto, uniqueLeaveId);
            ResponseDtoWrapper<LeaveResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", leaveResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cancelLeaveRequest")
    public ResponseEntity cancelLeaveRequest(@RequestBody String password, @RequestParam UUID uniqueLeaveId) {

        try{
            String reponse_message = leaveService.cancelLeaveRequest(password, uniqueLeaveId);
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(1,
                    "Success", reponse_message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
