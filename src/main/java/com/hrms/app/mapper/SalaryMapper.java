package com.hrms.app.mapper;

import com.hrms.app.dto.responseDto.SalaryResponseDto;
import com.hrms.app.entity.Employee;

public class SalaryMapper {

    public static SalaryResponseDto EmployeeToSalaryResponseDto(Employee employee) {

        return SalaryResponseDto.builder()
                .empName(employee.getEmpName())
                .empEmail(employee.getEmpEmail())
                .fixedSalary(employee.getEmpSalary())
                .build();
    }
}
