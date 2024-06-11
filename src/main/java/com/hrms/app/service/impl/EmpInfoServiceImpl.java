package com.hrms.app.service.impl;


import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.requestDto.EmployeeRequestDto;
import com.hrms.app.dto.requestDto.EmployeeUpdateRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.entity.Designation;
import com.hrms.app.entity.Employee;
//import com.hrms.app.entity.EmployeeType;
import com.hrms.app.mapper.EmployeeMapper;
import com.hrms.app.repository.DesignationRepository;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.service.EmpInfoService;
import com.hrms.app.service.FirebaseService;
import com.hrms.app.utilData.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class EmpInfoServiceImpl implements EmpInfoService {

    @Autowired
    private EmpInfoRepository empInfoRepository;

    @Autowired
    private UtilityDataServiceImpl utilityDataService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    DesignationRepository designationRepository;


    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto) throws Exception {

        if (employeeRequestDto != null) {

            Employee employee = EmployeeMapper.employeeRequestDtoToEmployee(employeeRequestDto);

            Map<EmployeeType, Integer> casualLeaveMap = utilityDataService.getUtilityData()
                                                                          .getMonthlyLeaveAllocationMap();

            if(!casualLeaveMap.containsKey(employee.getEmpType()))
                throw new RuntimeException("Invalid Employee type, Add given Employee Type first");

            employee.setLeaveCredited(casualLeaveMap.get(employee.getEmpType()));

            employee.setOptionalLeavesLeft(firebaseService.getOptionalLeavesAllowed());

            employee.setNationalLeavesLeft(firebaseService.getNationalLeavesAllowed());

            employee.setCasualLeavesLeft(employee.getLeaveCredited());

            Designation designation = designationRepository.findByDesignation(employeeRequestDto.getEmpDesignation());
            if(designation == null)
                throw new RuntimeException("Designation not found");

            employee.setEmpDesignation(designation);
            designation.getEmployeeList().add(employee);

            designationRepository.save(designation);
//            Employee savedEmployee = empInfoRepository.save(employee);

            return EmployeeMapper.employeeToEmployeeResponseDto(employee);

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

        if(employeeUpdateRequestDto.getEmpDesignation() != null) {
            //employee.setEmpDesignation(employeeUpdateRequestDto.getEmpDesignation());
            Designation designation = designationRepository.findByDesignation(employeeUpdateRequestDto.getEmpDesignation());
            if(designation == null)
                throw new RuntimeException("Designation not found");

            employee.setEmpDesignation(designation);
            designation.getEmployeeList().add(employee);
        }

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

        if (!employee.isActiveEmployee())
            throw new RuntimeException("Employee is already suspended");

        employee.setActiveEmployee(false);

        empInfoRepository.save(employee);

        return employee.getEmpName() + " (" + employee.getEmpDesignation().getDesignation() + ") with email "
                + empEmail + " is suspended ";
    }

    @Override
    public PageResponseDto getAllEmployee(int pageNo, String sortBy, String order) throws Exception {

//        int pageSize;

        int pageSize = firebaseService.getPageSizeEmp();

        Sort.Direction direction = order.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<Employee> page  = empInfoRepository.findAll(PageRequest.of(pageNo-1, pageSize, Sort.by(direction, sortBy)));

        List<Employee> employeeList = page.getContent();

        if (employeeList.isEmpty()) {
            throw new RuntimeException("Employees not found");
        }

        List<EmployeeResponseDto> employeeResponseDtoList = new ArrayList<>();

        for (Employee employee : employeeList) {
            employeeResponseDtoList.add(EmployeeMapper.employeeToEmployeeResponseDto(employee));
        }

        return new PageResponseDto(employeeResponseDtoList, page.getNumber()+1, page.getSize(),
                                page.getTotalPages(), page.getTotalElements(), page.isLast());
    }

    @Override
    public String employeeLogin(String empEmail, String password) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.isActiveEmployee()) {
            throw new RuntimeException("Sorry, Your don't have the necessary access privilege");
        }

        if (employee.getEmpPassword().equals(password))
            return "Welcome " + employee.getEmpName() + " , you are now logged in!";

        return "Invalid login password";
    }

    @Override
    public String applyForCompensationWorkDay(String empEmail, LocalDate date) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if (!date.getDayOfWeek().equals(DayOfWeek.SUNDAY) || date.isBefore(LocalDate.now()))
            throw new RuntimeException("Compensation work is only allowed on Sundays, Please choose" +
                    " a date accordingly");

        List<LocalDate> dateList = employee.getDateList();
        List<LeaveStatus> statusList = employee.getStatusList();


        if (dateList.contains(date))
            throw new RuntimeException("Cannot apply two times for the same date");

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

        if (date.isBefore(LocalDate.now()))
            throw new RuntimeException("Select a valid date occurring after today");

        List<LocalDate> dateList = employee.getDateList();
        List<LeaveStatus> statusList = employee.getStatusList();

        if (!dateList.contains(date)) {
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

        if (!dateList.contains(date)) {
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
    public void updateLeaveLeft() throws Exception {

        List<Employee> employeeList = empInfoRepository.findAll();

        for (Employee employee : employeeList) {

            employee.setCasualLeavesLeft(employee.getLeaveCredited());
            employee.setFlexiLeavesLeft(Constants.flexiLeave);
            employee.setPersonalLeavesLeft(Constants.personalLeave);
            employee.setOptionalLeavesLeft(firebaseService.getOptionalLeavesAllowed());
            employee.setNationalLeavesLeft(firebaseService.getNationalLeavesAllowed());
            employee.getDateList().clear();
            employee.getStatusList().clear();

            empInfoRepository.save(employee);
        }
    }

    @Scheduled(cron = "0 0 12 * * SUN", zone = "Asia/Calcutta")
    public void recordCompensationDay() {

        List<Employee> employeeList = empInfoRepository.getPresentEmployeeList();

        for (Employee employee : employeeList) {

            List<LocalDate> dateList = employee.getDateList();
            List<LeaveStatus> statusList = employee.getStatusList();
            
            if (dateList.contains(LocalDate.now()) && statusList.get(dateList.indexOf(LocalDate.now()))
                                                                .equals(LeaveStatus.APPROVED)) {

                employee.setNoOfCompensationWorkDayLeft(employee.getNoOfCompensationWorkDayLeft() - 1);
                employee.getCompensationWorkDayList().add(LocalDate.now());

                empInfoRepository.save(employee);
            }
        }
    }

}

