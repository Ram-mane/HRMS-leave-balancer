package com.hrms.app.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
import com.hrms.app.config.FirebaseInitialization;
import com.hrms.app.dto.requestDto.LeaveRequestDto;
import com.hrms.app.dto.responseDto.EmployeeLeaveResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Leave;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.mapper.EmployeeMapper;
import com.hrms.app.mapper.LeaveMapper;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.LeaveRepository;
import com.hrms.app.service.FirebaseService;
import com.hrms.app.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmpInfoRepository empInfoRepository;

    @Autowired
    private UtilityDataServiceImpl utilityDataService;

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public LeaveResponseDto applyLeave(LeaveRequestDto leaveRequestDto) {

        if(leaveRequestDto == null)
            throw new RuntimeException("Invalid Leave Request");

        if(!leaveRequestDto.getLeaveStartDate().isAfter(LocalDate.now()) || leaveRequestDto.getLeaveEndDate().isBefore(leaveRequestDto.getLeaveStartDate()))
            throw new RuntimeException("Enter valid leave duration dates");

        Employee employee = empInfoRepository.findByEmpEmail(leaveRequestDto.getEmployeeEmail());

        if(employee == null)
            throw new RuntimeException("Invalid Employee Email");


        Leave leave = LeaveMapper.leaveRequestDtoToLeave(leaveRequestDto);

        long days = DAYS.between(leaveRequestDto.getLeaveStartDate(), leaveRequestDto.getLeaveEndDate()) + 1;
        long leave_duration = (long)leave.getLeaveDuration();
        if(leave.getLeaveDuration() >= (double)1.0 && days != leave_duration) {
            throw new RuntimeException("Duration and Leave Dates didn't match");
        }

        leave.setEmployee(employee);

        employee.getLeaveList().add(leave);

        checkLeaveValidity(leave);

        empInfoRepository.save(employee);

       // Optional<Leave> opLeave1 = leaveRepository.findByEmployeeEmailAndDate(employee.getEmpEmail(), LocalDate.now());

        return LeaveMapper.leaveToLeaveResponseDto(leave);
    }

    @Override
    public PageResponseDto getPendingLeaveRequest(int pageNo) throws Exception{

        int pageSize = firebaseService.getPageSizeLeave();

        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        Page<Leave> page = leaveRepository.getPendingLeaveRequest(pageable);

//        if(leaveList.isEmpty())
//            throw new RuntimeException("There are no pending leaves right now");

        List<LeaveResponseDto> leaveResponseDtoList = new ArrayList<>();
        for(Leave leave : page) {
            leaveResponseDtoList.add(LeaveMapper.leaveToLeaveResponseDto(leave));
        }

       return new PageResponseDto(leaveResponseDtoList, page.getNumber()+1, page.getSize(),
                                page.getTotalPages(), page.getTotalElements(), page.isLast());
    }

    @Override
    public PageResponseDto getApprovedLeaveRequest(int pageNo) throws Exception{

        int pageSize = firebaseService.getPageSizeLeave();

        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        Page<Leave> page = leaveRepository.getApprovedLeaveRequest(pageable);

//        if(page.isEmpty())
//            throw new RuntimeException("There are no approved leaves right now");

        List<LeaveResponseDto> leaveResponseDtoList = new ArrayList<>();
        for(Leave leave : page) {
            leaveResponseDtoList.add(LeaveMapper.leaveToLeaveResponseDto(leave));
        }

        return new PageResponseDto(leaveResponseDtoList, page.getNumber()+1, page.getSize(),
                                    page.getTotalPages(), page.getTotalElements(), page.isLast());
    }

    @Override
    public PageResponseDto getAllLeaveRequest(String empEmail, int pageNo, LeaveStatus leaveStatus) throws Exception{

        int pageSize = firebaseService.getPageSizeLeave();

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if(employee == null)
            throw new RuntimeException("Invalid Employee email id");

        List<Leave> leaveList = employee.getLeaveList();

        if(leaveList.isEmpty())
            throw new RuntimeException("There are no leaves request right now");

        if(leaveStatus != null) {
            leaveList = leaveList.stream()
                    .filter(leave -> leave.getLeaveStatus() == leaveStatus)
                    .collect(Collectors.toList());
        }

        int totalPages = (int) Math.ceil((double) leaveList.size() / pageSize);

        // Check if requested page number exceeds total pages
        if (pageNo > totalPages) {
            // Return an empty page or handle as appropriate for your application
            return new PageResponseDto(new ArrayList<>(), pageNo, pageSize, totalPages, leaveList.size(), true);
        }

        int start = (pageNo-1)*pageSize;
        int end = Math.min(start+pageSize, leaveList.size());
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Leave> page = new PageImpl<>(leaveList.subList(start, end), pageable, leaveList.size());

        List<LeaveResponseDto> leaveResponseDtoList = new ArrayList<>();
        for(Leave leave : page) {
            leaveResponseDtoList.add(LeaveMapper.leaveToLeaveResponseDto(leave));
        }

        return new PageResponseDto(leaveResponseDtoList, page.getNumber()+1, page.getSize(), page.getTotalPages(),
                                    page.getTotalElements(), page.isLast());
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
        Leave appliedLeave = leaveRepository.findByUniqueLeaveId(uniqueLeaveId);

        if(appliedLeave == null)
            throw new RuntimeException("Incorrect leave Id");

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

    @Override
    public EmployeeLeaveResponseDto getNoOfLeavesLeft(String empEmail) {
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        return EmployeeMapper.employeeToEmployeeLeaveResponseDto(employee);
    }

    public void checkLeaveValidity(Leave appliedLeave) {

        if(appliedLeave.getLeaveStartDate().isBefore(LocalDate.now()))
            throw new RuntimeException("Request has became invalid now");

        Employee employee = appliedLeave.getEmployee();

        LeaveType leaveType = appliedLeave.getLeaveType();

        double duration = appliedLeave.getLeaveDuration();

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
