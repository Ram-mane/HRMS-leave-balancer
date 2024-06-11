package com.hrms.app.service.impl;

import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.entity.Designation;
import com.hrms.app.entity.Employee;
import com.hrms.app.mapper.EmployeeMapper;
import com.hrms.app.repository.DesignationRepository;
import com.hrms.app.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    DesignationRepository designationRepository;

    @Override
    public String addDesignation(String designation) {

        Designation designation1 = new Designation(designation);

        designationRepository.save(designation1);

        return "Designation of Type " + designation + " is created successfully";
    }

    @Override
    public List<String> getAllDesignation() {
        List<Designation> designationList = designationRepository.findAll();

        List<String> list = new ArrayList<>();

        for(Designation designation : designationList) {
            list.add(designation.getDesignation());
        }

        return list;

    }

    @Override
    public List<EmployeeResponseDto> getAllEmpWithDesignation(String designation) {

        Designation designation1 = designationRepository.findByDesignation(designation);

        if(designation1 == null)
            throw new RuntimeException("No such Designation exist");

        if(designation1.getEmployeeList().isEmpty())
            throw new RuntimeException("There is no employee with "+ designation+ " designation.");

//        List<EmployeeResponseDto> list = new ArrayList<>();
//        for (Employee employee : designation1.getEmployeeList()) {
//            list.add(EmployeeMapper.employeeToEmployeeResponseDto(employee)); }
//        return list;

        return designation1.getEmployeeList().stream().map(EmployeeMapper::employeeToEmployeeResponseDto).toList();
    }

}
