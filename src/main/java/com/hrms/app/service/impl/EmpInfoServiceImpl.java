package com.hrms.app.service.impl;


import com.hrms.app.Enum.AttendanceStatus;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Employee;
import com.hrms.app.mapper.EmployeeMapper;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.service.EmpInfoService;
import com.hrms.app.utilData.Constants;
import com.hrms.app.utilData.UtilReferenceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmpInfoServiceImpl implements EmpInfoService {

    @Autowired
    private EmpInfoRepository empInfoRepository;

    @Autowired
    private UtilityDataServiceImpl utilityDataService;


    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto) {

        if (employeeRequestDto != null) {

            Employee employee = EmployeeMapper.employeeRequestDtoToEmployee(employeeRequestDto);

            // int casualLeave = UtilReferenceData.monthlyLeaveAllocationMap.get(employee.getEmpType());

            Map<EmployeeType, Integer> casualLeaveMap = utilityDataService.getUtilityData()
                    .getMonthlyLeaveAllocationMap();

            employee.setLeaveCredited(casualLeaveMap.get(employee.getEmpType()));

            employee.setCasualLeavesLeft(employee.getLeaveCredited());

            Employee savedEmployee = empInfoRepository.save(employee);

            return EmployeeMapper.employeeToEmployeeResponseDto(savedEmployee);

        }
        return null;
    }

    @Override
    public EmployeeResponseDto getEmployee(String empEmail) {

        if (empEmail != null) {
            Employee employee = empInfoRepository.findByEmpEmail(empEmail);
            if (employee == null)
                throw new RuntimeException("Invalid Employee EmailId");
            return EmployeeMapper.employeeToEmployeeResponseDto(employee);
        }
        return null;
    }

    @Override
    public EmployeeResponseDto updateEmployee(String empEmail, EmployeeUpdateRequestDto employeeUpdateRequestDto) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        employee = EmployeeMapper.employeeUpdatedRequestDtoToEmployee(employee, employeeUpdateRequestDto);

        if (employeeUpdateRequestDto.getEmpType() != null) {
            Map<EmployeeType, Integer> casualLeaveMap = utilityDataService.getUtilityData()
                    .getMonthlyLeaveAllocationMap();

            employee.setLeaveCredited(casualLeaveMap.get(employee.getEmpType()));
        }

        Employee savedEmployee = empInfoRepository.save(employee);

        return EmployeeMapper.employeeToEmployeeResponseDto(savedEmployee);

    }

    @Override
    public String suspendEmployee(String empEmail) {
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        employee.setActiveEmployee(false);

        empInfoRepository.save(employee);

        return employee.getEmpName() + " (" + employee.getEmpDesignation() + ") with email "
                + empEmail + " is suspended ";
    }

    @Override
    public List<EmployeeResponseDto> getAllEmployee() {

        List<Employee> employeeList = empInfoRepository.findAll();

        if (employeeList.isEmpty()) {
            throw new RuntimeException("Employees not found");
        }

        List<EmployeeResponseDto> list = new ArrayList<>();

        for (Employee employee : employeeList) {
            list.add(EmployeeMapper.employeeToEmployeeResponseDto(employee));
        }

        return list;
    }

    @Override
    public String employeeLogin(String empEmail, String password) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if (employee.getEmpPassword().equals(password) && employee.isActiveEmployee())
            return "Welcome " + employee.getEmpName() + " , you are now logged in!";

        return "Invalid login password";
    }

    @Override
    public String applyForCompensationWorkDay(String empEmail, LocalDate date) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if (!date.getDayOfWeek().equals(DayOfWeek.SUNDAY) || date.isAfter(LocalDate.now()))
            throw new RuntimeException("Compensation work is only allowed on Sundays, Please choose" +
                    " a date accordingly");

        List<LocalDate> dateList = employee.getDateList();
        List<LeaveStatus> statusList = employee.getStatusList();

        dateList.add(date);
        statusList.add(dateList.indexOf(date), LeaveStatus.PENDING);

        empInfoRepository.save(employee);

        return "Request for compensation work day recorded successfully";
    }

    @Override
    public String approveCompensationWorkDay(String empEmail, LocalDate date) {
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        List<LocalDate> dateList = employee.getDateList();
        List<LeaveStatus> statusList = employee.getStatusList();

        if (dateList.indexOf(date) == -1) {
            throw new RuntimeException("No record for given date is found");
        }

        dateList.add(date);
        statusList.add(dateList.indexOf(date), LeaveStatus.APPROVED);

        employee.setNoOfCompensationWorkDayLeft(employee.getNoOfCompensationWorkDayLeft()+1);

        empInfoRepository.save(employee);

        return "Request for compensation work day has been Approved";
    }

    @Override
    public String rejectCompensationWorkDay(String empEmail, LocalDate date) {
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        List<LocalDate> dateList = employee.getDateList();
        List<LeaveStatus> statusList = employee.getStatusList();

        if (dateList.indexOf(date) == -1) {
            throw new RuntimeException("No record for given date is found");
        }

        dateList.add(date);
        statusList.add(dateList.indexOf(date), LeaveStatus.REJECTED);

        empInfoRepository.save(employee);

        return "Request for compensation work day has been Rejected";
    }

    @Override
    public Map<LocalDate, LeaveStatus> getCompensationWorkDay(String empEmail) {
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        List<LocalDate> dateList = employee.getDateList();
        List<LeaveStatus> statusList = employee.getStatusList();
        Map<LocalDate, LeaveStatus> compensationDayList = new HashMap<>();

        if (dateList.isEmpty()) {
            throw new RuntimeException("No record is found");
        }

        for (LocalDate date : dateList) {
            compensationDayList.put(date, statusList.get(dateList.indexOf(date)));
        }

        return compensationDayList;
    }

    @Override
    public Map<LeaveType, Integer> getNoOfLeavesLeft(String empEmail) {
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        Map<LeaveType, Integer> leavesList = new HashMap<>();

        int noOfCompensationDay = employee.getCompensationWorkDayList().size();

        leavesList.put(LeaveType.CASUAL, employee.getCasualLeavesLeft() + noOfCompensationDay);
        leavesList.put(LeaveType.FLEXI, employee.getFlexiLeavesLeft());
        leavesList.put(LeaveType.PERSONAL, employee.getPersonalLeavesLeft());
        leavesList.put(LeaveType.OPTIONAL, employee.getOptionalLeavesLeft());
        leavesList.put(LeaveType.NATIONAL, employee.getNationalLeavesLeft());

        return leavesList;
    }

    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Calcutta")
    public void updateCasualLeaveLeft() {

        List<Employee> employeeList = empInfoRepository.findAll();

        for (Employee employee : employeeList) {

            employee.setCasualLeavesLeft(employee.getCasualLeavesLeft() + employee.getLeaveCredited());
            employee.getCompensationWorkDayList().clear();
            empInfoRepository.save(employee);
        }
    }

    @Scheduled(cron = "0 0 0 1 1 *", zone = "Asia/Calcutta")
    public void updateLeaveLeft() {

        List<Employee> employeeList = empInfoRepository.findAll();

        for (Employee employee : employeeList) {

            employee.setCasualLeavesLeft(employee.getLeaveCredited());
            employee.setFlexiLeavesLeft(Constants.flexiLeave);
            employee.setPersonalLeavesLeft(Constants.personalLeave);
            employee.setOptionalLeavesLeft(Constants.optionalLeave);
            employee.setNationalLeavesLeft(Constants.nationalLeave);
            employee.getDateList().clear();
            employee.getStatusList().clear();

            empInfoRepository.save(employee);
        }
    }

}

