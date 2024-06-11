package com.hrms.app.service.impl;

import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Leave;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.entity.LeavePolicy;
import com.hrms.app.mapper.EmployeeMapper;
import com.hrms.app.mapper.LeaveMapper;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.LeaveRepository;
import com.hrms.app.service.LeaveService;
import com.hrms.app.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmpInfoRepository empInfoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private PaginationService paginationService;

    @Override
    public LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto) {

        if(leaveRequestDto == null)
            throw new RuntimeException("Invalid Leave Request");

        Employee employee = empInfoRepository.findByEmpEmail(leaveRequestDto.getEmployeeEmail());

        if(employee == null)
            throw new RuntimeException("Invalid Employee Email");

        Leave leave = LeaveMapper.leaveRequestDtoToLeave(leaveRequestDto);

        leave.setEmployee(employee);

        checkLeaveValidity(leave);

        employee.getLeaveList().add(leave);

        empInfoRepository.save(employee);

        return LeaveMapper.leaveToLeaveResponseDto(leave);
    }

    @Override
    public PageResponseDto getAllLeaveRequest(int pageNo, LeaveStatus leaveStatus, UUID organizationCode) throws Exception{

        List<Leave> leaveList = leaveRepository.findLeavesByOrganizationCodeAndStatus(organizationCode, leaveStatus);

        if(leaveList.isEmpty())
            throw new RuntimeException("There are no leaves request right now");

        return paginationService.paginationOnLeaveList(pageNo, leaveList);

    }

    @Override
    public PageResponseDto getAllLeaveRequestOfEmp(String empEmail, int pageNo, LeaveStatus leaveStatus, UUID organizationCode) throws Exception{

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if(employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        List<Leave> leaveList = leaveRepository.findLeavesByEmployeeEmailAndStatus(empEmail, leaveStatus);

        if(leaveList.isEmpty())
            throw new RuntimeException("There are no leaves request right now");

        return paginationService.paginationOnLeaveList(pageNo, leaveList);
    }


    @Override
    public LeaveResponseDto approveOrRejectLeave(UUID uniqueLeaveId, LeaveStatus leaveStatus) {

        Leave appliedLeave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);

        if(appliedLeave == null)
            throw new RuntimeException("Incorrect leave Id");

        if (leaveStatus.equals(LeaveStatus.APPROVED))
            approvingLeaveRequest(appliedLeave);

        appliedLeave.setLeaveStatus(leaveStatus);

        leaveRepository.save(appliedLeave);

        return LeaveMapper.leaveToLeaveResponseDto(appliedLeave);

    }

    @Override
    public LeaveResponseDto getLeaveRequest(UUID uniqueLeaveId) {

        Leave appliedLeave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);

        if(appliedLeave == null)
            throw new RuntimeException("Incorrect leave Id");

        return LeaveMapper.leaveToLeaveResponseDto(appliedLeave);

    }

    @Override
    public EmployeeLeaveResponseDto getNoOfLeavesLeft(String empEmail, UUID organizationCode) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        return EmployeeMapper.employeeToEmployeeLeaveResponseDto(employee);
    }

    @Override
    public LeaveResponseDto updateLeave(LeaveRequestDto leaveRequestDto, UUID uniqueLeaveId) {

        if(leaveRequestDto == null)
            throw new RuntimeException("Invalid Leave Request");

        Leave prevLeave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);

        if(prevLeave == null)
            throw new RuntimeException("Incorrect leave Id");

        if(prevLeave.getLeaveStatus().equals(LeaveStatus.APPROVED) && prevLeave.getLeaveStartDate().isAfter(LocalDate.now()))
            throw new RuntimeException("Leave updation period has now ended");

        Employee employee = empInfoRepository.findByEmpEmail(leaveRequestDto.getEmployeeEmail());

        if(employee == null)
            throw new RuntimeException("Invalid Employee Email");

        if (prevLeave.getLeaveStatus().equals(LeaveStatus.APPROVED))
            resetLeavesLeftToBeforeApprovedState(prevLeave);

        Leave leave = LeaveMapper.updateLeaveRequest(leaveRequestDto, prevLeave);

        checkLeaveValidity(leave);

        empInfoRepository.save(employee);

        return LeaveMapper.leaveToLeaveResponseDto(leave);
    }

    @Override
    public String cancelLeaveRequest(String password, UUID uniqueLeaveId) {

        Leave leave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);

        if(leave == null)
            throw new RuntimeException("Incorrect leave Id");

        if(leave.getLeaveStatus().equals(LeaveStatus.APPROVED) && leave.getLeaveStartDate().isAfter(LocalDate.now()))
            throw new RuntimeException("Duration to delete leave request has now ended");

        Employee employee = leave.getEmployee();

        if(!passwordEncoder.encode(password).equals(employee.getPassword()))
            throw new RuntimeException("Invalid Employee Password");

        if (leave.getLeaveStatus().equals(LeaveStatus.APPROVED))
            resetLeavesLeftToBeforeApprovedState(leave);

        employee.getLeaveList().remove(leave);

        empInfoRepository.save(employee);

        return "Your leave request with leave Id "+ leave.getUniqueLeaveId() +" has been removed successfully";
    }

    public void checkLeaveValidity(Leave appliedLeave) {

        if(!appliedLeave.getLeaveStartDate().isAfter(LocalDate.now()) ||
                appliedLeave.getLeaveEndDate().isBefore(appliedLeave.getLeaveStartDate()))
            throw new RuntimeException("Enter valid leave duration dates");

        Employee employee = appliedLeave.getEmployee();

        LeavePolicy leavePolicy = employee.getOrganization().getLeavePolicy();
        if(leavePolicy == null)
            throw new RuntimeException("Leave Policy for your organization is not found");

        LeaveType leaveType = appliedLeave.getLeaveType();

        double duration = appliedLeave.getLeaveDuration();

        LocalDate leaveDate = appliedLeave.getLeaveStartDate();

        long days = DAYS.between(leaveDate, appliedLeave.getLeaveEndDate()) + 1;
        long leave_duration = appliedLeave.getLeaveDuration().longValue();

        if(appliedLeave.getLeaveDuration() >= (double)1.0 && days != leave_duration) {
            throw new RuntimeException("Duration and Leave Dates didn't match");
        }

        if (duration > 1 && !(leaveType.equals(LeaveType.CASUAL) || leaveType.equals(LeaveType.MISCELLANEOUS_DURATION))) {
            throw new RuntimeException("Duration must be 1 day for " + leaveType + "leaves");
        }

        if(leavePolicy.getPersonalLeaveAllowed() && leaveType.equals(LeaveType.PERSONAL)) {
            if(!leaveDate.isEqual(employee.getDateOfBirth()) && !leaveDate.isEqual(employee.getJoiningDate()))
                throw new RuntimeException("Personal leaves are only for birthday and work Anniversary");
        }

        else if(leavePolicy.getFlexiLeaveAllowed() && leaveType.equals(LeaveType.FLEXI)) {
            LocalDate firstAllowDate = LocalDate.now();
            if(employee.getLastFlexiLeaveTaken() != null)
                firstAllowDate = employee.getLastFlexiLeaveTaken().
                        plusMonths(leavePolicy.getNoOfMonthsBetweenTwoFlexiLeave());

            if(leaveDate.isBefore(firstAllowDate))
                throw new RuntimeException("You have exhausted your Flexi leave for now, Please apply on/after " + firstAllowDate);
        }

        else if(leavePolicy.getOptionalLeaveAllowed() && leaveType.equals(LeaveType.OPTIONAL)) {
            if (employee.getOptionalLeavesLeft() == 0)
                throw new RuntimeException("You have already taken maximum allowed Optional holidays");

           // List<MonthDay> optionalLeaves = employee.getOrganization().getLeavePolicy().getOptionalLeaves();

            int month = leaveDate.getMonthValue();
            int day = leaveDate.getDayOfMonth();

//            if (!optionalLeaves.contains(MonthDay.of(month, day)))
//                throw new RuntimeException("Optional holiday cannot be granted on " + leaveDate);
        }

        else if(leaveType.equals(LeaveType.NATIONAL)) {
         //   List<MonthDay> nationalLeaves = employee.getOrganization().getLeavePolicy().getNationalLeaves();

            int month = leaveDate.getMonthValue();
            int day = leaveDate.getDayOfMonth();
//
//            if (!nationalLeaves.contains(MonthDay.of(month, day)))
//                throw new RuntimeException("National holiday cannot be granted on " + leaveDate);
        }

        else if(leaveType.equals(LeaveType.MISCELLANEOUS_SINGLE_DAY)) {
            if(!leavePolicy.getSpecificDateList().contains(leaveDate))
                throw new RuntimeException("Invalid date");
        }
    }

    public void approvingLeaveRequest(Leave appliedLeave) {

        if(appliedLeave.getLeaveStartDate().isBefore(LocalDate.now()))
            throw new RuntimeException("Request has became invalid now");

        Employee employee = appliedLeave.getEmployee();

        LeaveType leaveType = appliedLeave.getLeaveType();

        double duration = appliedLeave.getLeaveDuration();

        LocalDate leaveDate = appliedLeave.getLeaveStartDate();

        if(leaveType.equals(LeaveType.CASUAL)) {
            employee.setCasualLeavesLeft(employee.getCasualLeavesLeft() - duration);
        }

        else if(leaveType.equals(LeaveType.PERSONAL))
            employee.setPersonalLeavesLeft(employee.getPersonalLeavesLeft()-1);


        else if(leaveType.equals(LeaveType.FLEXI)) {
           employee.setLastFlexiLeaveTaken(leaveDate);
           employee.setFlexiLeavesLeft(employee.getFlexiLeavesLeft()-1);
        }

        else if(leaveType.equals(LeaveType.OPTIONAL))
            employee.setOptionalLeavesLeft(employee.getOptionalLeavesLeft()-1);

        else if(leaveType.equals(LeaveType.NATIONAL))
            employee.setNationalLeavesLeft(employee.getNationalLeavesLeft()-1);
    }

    private void resetLeavesLeftToBeforeApprovedState(Leave prevLeave) {

        Employee employee = prevLeave.getEmployee();

        LeaveType leaveType = prevLeave.getLeaveType();

        if(leaveType.equals(LeaveType.CASUAL)) {
            employee.setCasualLeavesLeft(employee.getCasualLeavesLeft() + prevLeave.getLeaveDuration());
        }

        else if(leaveType.equals(LeaveType.PERSONAL))
            employee.setPersonalLeavesLeft(employee.getPersonalLeavesLeft()+1);


        else if(leaveType.equals(LeaveType.FLEXI)) {
            employee.setLastFlexiLeaveTaken(null);
            employee.setFlexiLeavesLeft(employee.getFlexiLeavesLeft()+1);
        }

        else if(leaveType.equals(LeaveType.OPTIONAL))
            employee.setOptionalLeavesLeft(employee.getOptionalLeavesLeft()+1);

        else
            employee.setNationalLeavesLeft(employee.getNationalLeavesLeft()+1);

    }

    public int noOfLeavesTaken(String empEmail, LocalDate fromDate, LocalDate toDate) {

        List<Leave> leavesTakenList = leaveRepository.noOfLeavesTakenBetween(empEmail, fromDate, toDate, LeaveStatus.APPROVED);

        int leavesTaken = 0;
        for(Leave leave : leavesTakenList) {
            if(leave.getLeaveType().compareTo(LeaveType.CASUAL) == 0 || leave.getLeaveType().compareTo(LeaveType.MISCELLANEOUS_DURATION) == 0 ) {
                if(leave.getLeaveEndDate().isAfter(toDate))
                    leavesTaken += Duration.between(leave.getLeaveStartDate(), toDate).toDays()+1;
                else
                    leavesTaken += leave.getLeaveDuration()+1;
            }
            else
                leavesTaken += 1;
        }

        return leavesTaken;
    }
}
