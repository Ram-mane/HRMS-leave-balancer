package com.hrms.app.controller;


import com.hrms.app.entity.EmployeeInfo;
import com.hrms.app.service.EmpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/empInfo")
public class EmpInfoController {

    @Autowired
    private EmpInfoService empInfoService;

    @PostMapping("/addEmp")
    public ResponseEntity<EmployeeInfo> addEmployee(@RequestBody EmployeeInfo employeeInfo) {

        try{
            empInfoService.addEmployee(employeeInfo);
            return ResponseEntity.ok(employeeInfo);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getEmpInfo/")
    public ResponseEntity<EmployeeInfo> getEmployee(@Param("empEmail") String empEmail) {

        try{
            empInfoService.getEmployee(empEmail);

            return ResponseEntity.ok(empInfoService.getEmployee(empEmail));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
