package com.hrms.app.mapper;

import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.entity.Employee;
import com.hrms.app.utilData.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeMapper {

    public static Employee employeeRequestDtoToEmployee(EmployeeRequestDto employeeRequestDto) {

        Employee employee = Employee.builder()
                                    .empName(employeeRequestDto.getEmpName())
                                    .empEmail(employeeRequestDto.getEmpEmail())
                                    .empDesignation(employeeRequestDto.getEmpDesignation())
                                    .dateOfBirth(employeeRequestDto.getDateOfBirth())
                                    .empPassword(employeeRequestDto.getEmpPassword())
                                    .empPhone(employeeRequestDto.getEmpPhone())
                                    .empType(employeeRequestDto.getEmpType())
                                    .empSalary(employeeRequestDto.getEmpSalary())
                                    .imgUrl(employeeRequestDto.getImgUrl())
                                    .joiningDate(employeeRequestDto.getJoiningDate())
                                    .build();

        employee.setFlexiLeavesLeft(Constants.flexiLeave);
        employee.setNationalLeavesLeft(Constants.nationalLeave);
        employee.setOptionalLeavesLeft(Constants.optionalLeave);
        employee.setPersonalLeavesLeft(Constants.personalLeave);

        employee.setLastFlexiLeaveTaken(null);
        employee.setNoOfCompensationWorkDayLeft(0);
        employee.setDateList(new ArrayList<>());
        employee.setStatusList(new ArrayList<>());
        employee.setCompensationWorkDayList(new ArrayList<>());

        employee.setActiveEmployee(true);

        employee.setLeaveList(new ArrayList<>());
        employee.setAttendanceList(new ArrayList<>());
        employee.setAttendanceMarked(false);

        employee.setCreatedAt(LocalDateTime.now());
        employee.setCreatedBy("Admin");
        employee.setModifiedAt(LocalDateTime.now());
        employee.setModifiedBy("Admin");

        return employee;

    }

    public static EmployeeResponseDto employeeToEmployeeResponseDto(Employee employee) {

        return EmployeeResponseDto.builder()
                .empName(employee.getEmpName())
                .empDesignation(employee.getEmpDesignation())
                .empEmail(employee.getEmpEmail())
                .empPhone(employee.getEmpPhone())
                .empSalary(employee.getEmpSalary())
                .empType(employee.getEmpType())
                .imgUrl(employee.getImgUrl())
                .joiningDate(employee.getJoiningDate())
                .dateOfBirth(employee.getDateOfBirth())
                .activeEmployee(employee.isActiveEmployee())
                .build();

    }


    public static Employee employeeUpdatedRequestDtoToEmployee(Employee employee, EmployeeUpdateRequestDto employeeUpdateRequestDto) {

        if(employeeUpdateRequestDto.getEmpEmail() != null)
            employee.setEmpEmail(employeeUpdateRequestDto.getEmpEmail());

        if(employeeUpdateRequestDto.getEmpDesignation() != null)
            employee.setEmpDesignation(employeeUpdateRequestDto.getEmpDesignation());

        if(employeeUpdateRequestDto.getEmpPassword() != null)
            employee.setEmpPassword(employeeUpdateRequestDto.getEmpPassword());

        if(employeeUpdateRequestDto.getEmpName() != null)
            employee.setEmpName(employeeUpdateRequestDto.getEmpName());

        if(employeeUpdateRequestDto.getEmpPhone() != null)
            employee.setEmpPhone(employeeUpdateRequestDto.getEmpPhone());

        if(employeeUpdateRequestDto.getEmpSalary() == 0)
            employee.setEmpSalary(employeeUpdateRequestDto.getEmpSalary());

        if(employeeUpdateRequestDto.getEmpType() != null)
            employee.setEmpType(employeeUpdateRequestDto.getEmpType());

        if(employeeUpdateRequestDto.getImgUrl() != null)
            employee.setImgUrl(employeeUpdateRequestDto.getImgUrl());

        return employee;
    }
}
