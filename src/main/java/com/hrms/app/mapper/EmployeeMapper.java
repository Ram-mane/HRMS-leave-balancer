package com.hrms.app.mapper;

import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.LeavePolicy;
import com.hrms.app.utilData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeMapper {


    public static Employee employeeRequestDtoToEmployee(EmployeeRequestDto employeeRequestDto) {

        return Employee.builder()
                        .empName(employeeRequestDto.getEmpName())
                        .empEmail(employeeRequestDto.getEmpEmail())
                        .dateOfBirth(employeeRequestDto.getDateOfBirth())
                        .empPhone(employeeRequestDto.getEmpPhone())
                        .empType(employeeRequestDto.getEmpType())
                        .empSalary(employeeRequestDto.getEmpSalary())
                        .salaryRecord(new ArrayList<>())
//                        .imgUrl(employeeRequestDto.getImgUrl())
                        .joiningDate(employeeRequestDto.getJoiningDate())
                        .shiftNumber(employeeRequestDto.getShiftNumber()-1)
                        .compensationRequestList(new ArrayList<>())
                        .activeEmployee(true)
                        .leaveList(new ArrayList<>())
                        .attendanceList(new ArrayList<>())
                        .attendanceMarked(false)
                        .createdAt(LocalDateTime.now())
                        .createdBy("Admin")
                        .modifiedAt(LocalDateTime.now())
                        .modifiedBy("Admin")
                        .build();

    }

    public static EmployeeResponseDto employeeToEmployeeResponseDto(Employee employee) {

        return EmployeeResponseDto.builder()
                .empName(employee.getEmpName())
                .empEmail(employee.getEmpEmail())
                .empPhone(employee.getEmpPhone())
                .empSalary(employee.getEmpSalary())
                .empType(employee.getEmpType())
                .imgUrl(employee.getImgUrl())
                .joiningDate(employee.getJoiningDate())
                .dateOfBirth(employee.getDateOfBirth())
                .activeEmployee(employee.getActiveEmployee())
                .empDesignation(employee.getEmpDesignation().getDesignation())
                .shiftNumber(employee.getShiftNumber())
                .build();

    }


    public static Employee employeeUpdatedRequestDtoToEmployee(Employee employee, EmployeeUpdateRequestDto employeeUpdateRequestDto) {

        if(employeeUpdateRequestDto.getEmpEmail() != null)
            employee.setEmpEmail(employeeUpdateRequestDto.getEmpEmail());

//        if(employeeUpdateRequestDto.getEmpDesignation() != null)
//            employee.setEmpDesignation(employeeUpdateRequestDto.getEmpDesignation());

        if(employeeUpdateRequestDto.getEmpPassword() != null)
            employee.setEmpPassword(employeeUpdateRequestDto.getEmpPassword());

        if(employeeUpdateRequestDto.getEmpName() != null)
            employee.setEmpName(employeeUpdateRequestDto.getEmpName());

        if(employeeUpdateRequestDto.getEmpPhone() != null)
            employee.setEmpPhone(employeeUpdateRequestDto.getEmpPhone());

        if(employeeUpdateRequestDto.getEmpSalary() != null)//
            employee.setEmpSalary(employeeUpdateRequestDto.getEmpSalary());

        if(employeeUpdateRequestDto.getEmpType() != null)
            employee.setEmpType(employeeUpdateRequestDto.getEmpType());

        if(employeeUpdateRequestDto.getImgUrl() != null)
            employee.setImgUrl(employeeUpdateRequestDto.getImgUrl());

        if(employeeUpdateRequestDto.getShiftNumber() != null)
            employee.setShiftNumber(employeeUpdateRequestDto.getShiftNumber()-1);

        return employee;
    }

    public static EmployeeLeaveResponseDto employeeToEmployeeLeaveResponseDto(Employee employee) {
        return EmployeeLeaveResponseDto.builder()
                .empName(employee.getEmpName())
                .empEmail(employee.getEmpEmail())
                .empDesignation(employee.getEmpDesignation().getDesignation())
                .empPhone(employee.getEmpPhone())
                .empType(employee.getEmpType())
                .imgUrl(employee.getImgUrl())
                .casual_leaves_left(employee.getCasualLeavesLeft())
                .flexi_leaves_left(employee.getFlexiLeavesLeft())
                .personal_leaves_left(employee.getPersonalLeavesLeft())
                .optional_leaves_left(employee.getOptionalLeavesLeft())
                .national_leaves_left(employee.getNationalLeavesLeft())
                .build();
    }
}
