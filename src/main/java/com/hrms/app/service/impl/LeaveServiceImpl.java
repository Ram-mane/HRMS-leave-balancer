package com.hrms.app.service.impl;

import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Leave;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.mapper.LeaveMapper;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.LeaveRepository;
import com.hrms.app.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmpInfoRepository empInfoRepository;

    @Autowired
    private UtilityDataServiceImpl utilityDataService;

    @Override
    public LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto) {
        System.out.println("Yo i'm at start");
        if(leaveRequestDto == null)
            throw new RuntimeException("Invalid Leave Request");

        if(leaveRequestDto.getLeaveEndDate().isBefore(leaveRequestDto.getLeaveStartDate()))
            throw new RuntimeException("Enter valid leave duration dates");

        Employee employee = empInfoRepository.findByEmpEmail(leaveRequestDto.getEmployeeEmail());

        if(employee == null)
            throw new RuntimeException("Invalid Employee Email");

        Leave leave = LeaveMapper.leaveRequestDtoToLeave(leaveRequestDto);

        leave.setEmployee(employee);
        System.out.println("Yo i'm at 6");
        employee.getLeaveList().add(leave);
        System.out.println("Yo i'm at 7");
        empInfoRepository.save(employee);

      //  Leave savedLeave = employee.getLeaveList().get(0);
        System.out.println("Yo i'm at end");
        return LeaveMapper.leaveToLeaveResponseDto(leave);
    }

    @Override
    public List<LeaveResponseDto> getPendingLeaveRequest() {

       List<Leave> leaveList = leaveRepository.getPendingLeaveRequest();

       if(leaveList.isEmpty())
           throw new RuntimeException("There are no pending leaves right now");

       List<LeaveResponseDto> leaveResponseDtoList = new ArrayList<>();
       for(Leave leave : leaveList) {
           LeaveResponseDto leaveResponseDto = LeaveMapper.leaveToLeaveResponseDto(leave);
           leaveResponseDtoList.add(leaveResponseDto);
       }

       return leaveResponseDtoList;
    }

    @Override
    public List<LeaveResponseDto> getApprovedLeaveRequest() {

        List<Leave> leaveList = leaveRepository.getApprovedLeaveRequest();

        if(leaveList.isEmpty())
            throw new RuntimeException("There are no approved leaves right now");

        List<LeaveResponseDto> leaveResponseDtoList = new ArrayList<>();
        for(Leave leave : leaveList) {
            leaveResponseDtoList.add(LeaveMapper.leaveToLeaveResponseDto(leave));
        }

        return leaveResponseDtoList;
    }

    @Override
    public List<LeaveResponseDto> getAllLeaveRequest(String empEmail) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if(employee == null)
            throw new RuntimeException("Invalid Employee email id");

        List<Leave> leaveList = employee.getLeaveList();

        if(leaveList.isEmpty())
            throw new RuntimeException("There are no leaves request right now");

        List<LeaveResponseDto> leaveResponseDtoList = new ArrayList<>();
        for(Leave leave : leaveList) {
            leaveResponseDtoList.add(LeaveMapper.leaveToLeaveResponseDto(leave));
        }

        return leaveResponseDtoList;
    }


    @Override
    public LeaveResponseDto approveLeave(UUID uniqueLeaveId) {

        Leave appliedLeave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);

        if(appliedLeave == null)
            throw new RuntimeException("Incorrect leave Id");

        checkLeaveValidity(appliedLeave);

        appliedLeave.setLeaveStatus(LeaveStatus.APPROVED);

        leaveRepository.save(appliedLeave);

        return LeaveMapper.leaveToLeaveResponseDto(appliedLeave);

    }

    @Override
    public LeaveResponseDto getLeaveRequest(UUID uniqueLeaveId) {
        System.out.println("0");
        Leave appliedLeave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);
        System.out.println("1");
        if(appliedLeave == null)
            throw new RuntimeException("Incorrect leave Id");
        System.out.println("2");
        return LeaveMapper.leaveToLeaveResponseDto(appliedLeave);

    }

    @Override
    public LeaveResponseDto rejectLeave(UUID uniqueLeaveId) {

        Leave appliedLeave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);

        if(appliedLeave == null)
            throw new RuntimeException("Incorrect leave Id");

        appliedLeave.setLeaveStatus(LeaveStatus.REJECTED);

        leaveRepository.save(appliedLeave);

        return LeaveMapper.leaveToLeaveResponseDto(appliedLeave);

    }

    public void checkLeaveValidity(Leave appliedLeave) {

        Employee employee = appliedLeave.getEmployee();

        LeaveType leaveType = appliedLeave.getLeaveType();

        int duration = appliedLeave.getLeaveDuration();

        LocalDate leaveDate = appliedLeave.getLeaveStartDate();

        if (duration > 1 && leaveType.compareTo(LeaveType.CASUAL) != 0) {
            throw new RuntimeException("Duration must be 1 day for " + leaveType + "leaves");
        }

        if(leaveType.equals(LeaveType.CASUAL)) {
            employee.setCasualLeavesLeft(employee.getCasualLeavesLeft() - duration);
        }

        else if(leaveType.equals(LeaveType.PERSONAL)) {
            if(leaveDate.isEqual(employee.getDateOfBirth()) || leaveDate.equals(employee.getJoiningDate())) {
                employee.setPersonalLeavesLeft(employee.getPersonalLeavesLeft()-1);
            }
            else throw new RuntimeException("Personal leaves are only for birthday and work Anniversary");
        }

        else if(leaveType.equals(LeaveType.FLEXI)) {
            LocalDate firstAllowDate = LocalDate.now();
            if(employee.getLastFlexiLeaveTaken() != null)
                firstAllowDate = employee.getLastFlexiLeaveTaken().plusMonths(4);

            if(employee.getLastFlexiLeaveTaken() == null || leaveDate.isAfter(firstAllowDate) || leaveDate.isEqual(firstAllowDate)) {
                employee.setLastFlexiLeaveTaken(leaveDate);
                employee.setFlexiLeavesLeft(employee.getFlexiLeavesLeft()-1);
            }
            else throw new RuntimeException("Only 1 Flexi leave is allowed under 4 months apply on/after " + firstAllowDate);
        }

        else if(leaveType.equals(LeaveType.OPTIONAL)) {
            if (employee.getOptionalLeavesLeft() == 0)
                throw new RuntimeException("You have already taken maximum allowed Optional holidays");

            Map<LocalDate, String> optionalLeaveMap = utilityDataService.getUtilityData().getOptionalHolidays();

            if (optionalLeaveMap.containsKey(leaveDate)) {
                employee.setOptionalLeavesLeft(employee.getOptionalLeavesLeft()-1);
            }
            else throw new RuntimeException("Optional holiday cannot be granted on " + leaveDate);
        }

        else if(leaveType.equals(LeaveType.NATIONAL)) {
            Map<LocalDate, String> nationalLeaveMap = utilityDataService.getUtilityData().getNationalHolidays();
            if (nationalLeaveMap.containsKey(leaveDate)) {
                employee.setNationalLeavesLeft(employee.getNationalLeavesLeft()-1);
            }
            else throw new RuntimeException("National holiday cannot be granted on " + leaveDate);
        }

        else throw new RuntimeException("Invalid Leave type");
    }
}
