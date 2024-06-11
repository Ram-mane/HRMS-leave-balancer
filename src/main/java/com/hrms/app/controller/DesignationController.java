package com.hrms.app.controller;

import com.hrms.app.dto.responseDto.*;
import com.hrms.app.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/Designation")
public class DesignationController {

    @Autowired
    DesignationService designationService;

    @PostMapping("/add_designation")
    public ResponseEntity addDesignation(@RequestParam String designation) {
        try{
            String result = designationService.addDesignation(designation);
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(1,
                    "Success", result);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", "Employee type "+designation+" already exist");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_all_designation")
    public ResponseEntity getAllDesignation() {
        try{
            List<String> designationList = designationService.getAllDesignation();
            ResponseDtoListWrapper response = new ResponseDtoListWrapper(1,
                    "Success", designationList);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_all_emp_with_designation/{pageNo}")
    public ResponseEntity getAllEmpWithDesignation(@PathVariable int pageNo, @RequestParam String designation, @RequestParam UUID organizationCode) {
        try{
            PageResponseDto pageResponseDto = designationService.getAllEmpWithDesignation(designation, pageNo, organizationCode);
            ResponseDtoWrapper<PageResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", pageResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
