package com.hrms.app.controller;

//import com.hrms.app.entity.EmployeeType;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.service.UtilityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/util")
public class UtilityDataController {

    @Autowired
    private UtilityDataService utilityDataService;

    @PostMapping("/add_employee_type")
    public ResponseEntity addEmployeeType(@RequestParam EmployeeType employeeType, @RequestParam int noOfCasualLeave) {
        try {
            String message = utilityDataService.addEmployeeType(employeeType, noOfCasualLeave);
            return new ResponseEntity(message, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update_employee_type")
    public ResponseEntity updateEmployeeType(@RequestParam EmployeeType employeeType, @RequestParam int noOfCasualLeave) {
        try {
            String message = utilityDataService.updateEmployeeType(employeeType, noOfCasualLeave);
            return new ResponseEntity(message, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove_employee_type")
    public ResponseEntity removeEmployeeType(@RequestParam EmployeeType employeeType) {
        try {
            String message = utilityDataService.removeEmployeeType(employeeType);
            return new ResponseEntity(message, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_employee_type")
    public ResponseEntity getEmployeeType() {
        try {
            Map<EmployeeType, Integer> map = utilityDataService.getEmployeeType();
            return new ResponseEntity(map, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add_national_holiday")
    public ResponseEntity addNationalHoliday(@RequestParam String event, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
//        UtilReferenceData.nationalHolidays.put(event, date);
        try {
            String message = utilityDataService.addNationalHoliday(event, date);
            return new ResponseEntity(message, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update_national_holiday")
    public ResponseEntity updateNationalHoliday(@RequestParam String event, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        //UtilReferenceData.nationalHolidays.put(event, date);
        try {
            String message = utilityDataService.updateNationalHoliday(event, date);
            return new ResponseEntity(message, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove_national_holiday")
    public ResponseEntity removeNationalHoliday(@RequestParam String event) {
      //  UtilReferenceData.nationalHolidays.remove(event);
        try {
            String message = utilityDataService.removeNationalHoliday(event);
            return new ResponseEntity(message, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_national_holidays")
    public ResponseEntity getNationalHolidays() {
        try {
            Map<LocalDate, String> map = utilityDataService.getNationalHolidays();
            return new ResponseEntity(map, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add_optional_holiday")
    public ResponseEntity addOptionalHoliday(@RequestParam String event, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        //UtilReferenceData.optionalHolidays.put(event, date);
        try {
            String message = utilityDataService.addOptionalHoliday(event, date);
            return new ResponseEntity(message, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update_optional_holiday")
    public ResponseEntity updateOptionalHoliday(@RequestParam String event, @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        //UtilReferenceData.optionalHolidays.put(event, date);
        try {
            String message = utilityDataService.updateOptionalHoliday(event, date);
            return new ResponseEntity(message, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove_optional_holiday")
    public ResponseEntity removeOptionalHoliday(@RequestParam String event) {
//        UtilReferenceData.optionalHolidays.remove(event);
        try {
            String message = utilityDataService.removeOptionalHoliday(event);
            return new ResponseEntity(message, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get_optional_holidays")
    public ResponseEntity getOptionalHolidays() {
        try {
            Map<LocalDate, String> map = utilityDataService.getOptionalHolidays();
            return new ResponseEntity(map, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
