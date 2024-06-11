package com.hrms.app.controller;

import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/Designation")
public class DesignationController {

    @Autowired
    DesignationService designationService;

    @PostMapping("/add_designation")
    public ResponseEntity addDesignation(@RequestParam String designation) {
        try{
            String response = designationService.addDesignation(designation);
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Employee type "+designation+" already exist", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_all_designation")
    public ResponseEntity getAllDesignation() {
        try{
            List<String> designationList = designationService.getAllDesignation();
            return new ResponseEntity(designationList, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_all_emp_with_designation")
    public ResponseEntity getAllEmpWithDesignation(@RequestParam String designation) {
        try{
            List<EmployeeResponseDto> employeeResponseDtoList = designationService.getAllEmpWithDesignation(designation);
            return new ResponseEntity(employeeResponseDtoList, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
