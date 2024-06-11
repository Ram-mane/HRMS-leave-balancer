package com.hrms.app.service;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.requestDto.GetAllEmployeesRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
//import com.hrms.app.entity.Employee;
import com.hrms.app.dto.responseDto.SalaryResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface EmpInfoService {

    EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto) throws Exception;

    EmployeeResponseDto getEmployee(String empEmail, UUID organizationCode);

    EmployeeResponseDto updateEmployee(String empEmail, EmployeeUpdateRequestDto employeeUpdateRequestDto, UUID organizationCode);

    String suspendEmployee(String empEmail, UUID organizationCode);

    PageResponseDto getAllEmployee(GetAllEmployeesRequestDto getAllEmployeesRequestDto, UUID organizationCode) throws Exception;

    String changeEmpPassword(String empEmail, String newPassword, UUID organizationCode);

    SalaryResponseDto getEmployeeSalary(String empEmail, LocalDate fromDate, LocalDate toDate, UUID organizationCode);

    List<SalaryResponseDto> getAllEmployeeSalary(UUID organizationCode);

    SalaryResponseDto getEmployeeMonthlySalary(String empEmail, UUID organizationCode);
}
