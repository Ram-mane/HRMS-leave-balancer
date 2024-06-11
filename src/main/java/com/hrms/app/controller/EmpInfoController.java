package com.hrms.app.controller;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.requestDto.GetAllEmployeesRequestDto;
import com.hrms.app.dto.responseDto.*;
import com.hrms.app.service.EmpInfoService;
import com.hrms.app.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/empInfo")
public class EmpInfoController {

    @Autowired
    private EmpInfoService empInfoService;

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/addEmp")
    public ResponseEntity addEmployee(@ModelAttribute EmployeeRequestDto employeeRequestDto) {
//            employeeRequestDto.getEmpImage().getContentType()

        try{
            EmployeeResponseDto employeeResponseDto = empInfoService.addEmployee(employeeRequestDto);
            ResponseDtoWrapper<EmployeeResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", employeeResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
//            return new ResponseEntity<>(ResponseDtoWrapper.builder().statusCode(1).message("Success")
//                    .result(employeeResponseDto).build(), HttpStatus.OK);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//            return new ResponseEntity<>(ResponseDtoWrapper.builder().statusCode(0)
//                    .message("Failed").result(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getEmpInfo")
    public ResponseEntity getEmployee(@RequestParam String empEmail, @RequestParam UUID organizationCode) {

        try{
            EmployeeResponseDto employeeResponseDto = empInfoService.getEmployee(empEmail, organizationCode);
            ResponseDtoWrapper<EmployeeResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", employeeResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllEmpInfo")
    public ResponseEntity getAllEmployee(@RequestBody GetAllEmployeesRequestDto getAllEmployeesRequestDto, @RequestParam UUID organizationCode) {

        try{
            PageResponseDto pageResponseDto = empInfoService.getAllEmployee(getAllEmployeesRequestDto, organizationCode);
            ResponseDtoWrapper<PageResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", pageResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateEmployeeData")
    public ResponseEntity updateEmployee(@RequestParam String empEmail, @RequestBody EmployeeUpdateRequestDto employeeUpdateRequestDto,
                                         @RequestParam UUID organizationCode) {
        try {
            EmployeeResponseDto employeeResponseDto = empInfoService.updateEmployee(empEmail, employeeUpdateRequestDto, organizationCode);
            ResponseDtoWrapper<EmployeeResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", employeeResponseDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/suspendEmployee")
    public ResponseEntity suspendEmployee(@RequestParam String empEmail, @RequestParam UUID organizationCode) {
        try {
            String message = empInfoService.suspendEmployee(empEmail, organizationCode);
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(1,
                    "Success", message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changeEmpPassword")
    public ResponseEntity changeEmpPassword(@RequestParam String empEmail, @RequestParam String newPassword,
                                            @RequestParam UUID organizationCode) {
        try {
            String message = empInfoService.changeEmpPassword(empEmail, newPassword, organizationCode);
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(1,
                    "Success", message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getEmployeeMonthlySalary")
    public ResponseEntity getEmployeeMonthlySalary(@RequestParam String empEmail, @RequestParam UUID organizationCode) {

        try{
            SalaryResponseDto salaryResponseDto = empInfoService.getEmployeeMonthlySalary(empEmail, organizationCode);
            ResponseDtoWrapper<SalaryResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", salaryResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getEmployeeSalary")
    public ResponseEntity getEmployeeSalary(@RequestParam String empEmail, @RequestParam LocalDate fromDate,
                                            @RequestParam LocalDate toDate, @RequestParam UUID organizationCode) {

        try{
            SalaryResponseDto salaryResponseDto = empInfoService.getEmployeeSalary(empEmail, fromDate, toDate, organizationCode);
            ResponseDtoWrapper<SalaryResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", salaryResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllEmployeeSalary")
    public ResponseEntity getAllEmployeeSalary(@RequestParam UUID organizationCode) {

        try{
            List<SalaryResponseDto> salaryResponseDtoList = empInfoService.getAllEmployeeSalary(organizationCode);
            ResponseDtoListWrapper response = new ResponseDtoListWrapper(1,
                    "Success", salaryResponseDtoList);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
