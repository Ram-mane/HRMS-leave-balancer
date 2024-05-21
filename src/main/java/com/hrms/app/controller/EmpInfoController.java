package com.hrms.app.controller;


import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.entity.Employee;
import com.hrms.app.service.EmpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/empInfo")
public class EmpInfoController {

    @Autowired
    private EmpInfoService empInfoService;

    @PostMapping("/addEmp")
    public ResponseEntity addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {

        try{
            EmployeeResponseDto employeeResponseDto = empInfoService.addEmployee(employeeRequestDto);
            return new ResponseEntity<>(employeeResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getEmpInfo")
    public ResponseEntity getEmployee(@Param("empEmail") String empEmail) {

        try{
            EmployeeResponseDto employeeResponseDto = empInfoService.getEmployee(empEmail);
            return new ResponseEntity<>(employeeResponseDto, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllEmpInfo")
    public ResponseEntity getAllEmployee() {

        try{
            List<EmployeeResponseDto> employeeResponseDtoList = empInfoService.getAllEmployee();
            return new ResponseEntity<>(employeeResponseDtoList, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateEmployeeData")
    public ResponseEntity updateEmployee(@RequestParam String empEmail, @RequestBody EmployeeUpdateRequestDto employeeUpdateRequestDto) {
        try {
            EmployeeResponseDto employeeResponseDto = empInfoService.updateEmployee(empEmail, employeeUpdateRequestDto);
            return new ResponseEntity<>(employeeResponseDto, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/suspendEmployee")
    public ResponseEntity suspendEmployee(@RequestParam String empEmail) {
        try {
            String message = empInfoService.suspendEmployee(empEmail);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/employeeLogin")
    public ResponseEntity employeeLogin(@RequestParam String empEmail, @RequestParam String password) {

        try{
            String message = empInfoService.employeeLogin(empEmail, password);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/applyForCompensationWork")
    public ResponseEntity applyForCompensationWorkDay(@RequestParam String empEmail,
                                                      @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        try{
            String response = empInfoService.applyForCompensationWorkDay(empEmail, date);
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approveCompensationWorkDay")
    public ResponseEntity approveCompensationWorkDay(@RequestParam String empEmail,
                                                     @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        try{
            String response = empInfoService.approveCompensationWorkDay(empEmail, date);
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rejectCompensationWorkDay")
    public ResponseEntity rejectCompensationWorkDay(@RequestParam String empEmail,
                                                     @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        try{
            String response = empInfoService.rejectCompensationWorkDay(empEmail, date);
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCompensationWorkDay")
    public ResponseEntity getCompensationWorkDay(@RequestParam String empEmail) {
        try{
            Map<LocalDate, LeaveStatus> compensationWorkDayMap  = empInfoService.getCompensationWorkDay(empEmail);
            return new ResponseEntity(compensationWorkDayMap, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getNoOfLeavesLeft")
    public ResponseEntity getNoOfLeavesLeft(@RequestParam String empEmail) {
        try{
            Map<LeaveType, Integer> leavesList  = empInfoService.getNoOfLeavesLeft(empEmail);
            return new ResponseEntity(leavesList, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
