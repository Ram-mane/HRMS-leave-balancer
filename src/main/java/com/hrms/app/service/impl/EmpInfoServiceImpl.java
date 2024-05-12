package com.hrms.app.service.impl;


import com.hrms.app.entity.EmployeeInfo;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.service.EmpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmpInfoServiceImpl implements EmpInfoService {

    @Autowired
    private EmpInfoRepository empInfoRepository;


    @Override
    public EmployeeInfo addEmployee(EmployeeInfo employeeInfo) {

        if( employeeInfo != null){
            return empInfoRepository.save(employeeInfo);

        }
        return null;
    }

    @Override
    public EmployeeInfo getEmployee(String empEmail) {

        if(empEmail != null){
            return empInfoRepository.findByEmpEmail(empEmail);
        }
        return null;
    }


}
