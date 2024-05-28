package com.hrms.app.service;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
//import com.hrms.app.entity.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface EmpInfoService {

    EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto) throws Exception;

    EmployeeResponseDto getEmployee(String empEmail);

    EmployeeResponseDto updateEmployee(String empEmail, EmployeeUpdateRequestDto employeeUpdateRequestDto);

    String suspendEmployee(String empEmail);

    PageResponseDto getAllEmployee(int pageNo, String sortBy, String order) throws Exception;

    String employeeLogin(String empEmail, String password);

    String applyForCompensationWorkDay(String empEmail, LocalDate date);

    String approveCompensationWorkDay(String empEmail, LocalDate date);

    Map<LocalDate, LeaveStatus> getCompensationWorkDay(String empEmail);

    String rejectCompensationWorkDay(String empEmail, LocalDate date);
}
