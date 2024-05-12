package com.hrms.app.service;


import com.hrms.app.entity.EmployeeInfo;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface EmpInfoService {

    EmployeeInfo addEmployee(EmployeeInfo employeeInfo) ;


    EmployeeInfo getEmployee(String empEmail);
}
