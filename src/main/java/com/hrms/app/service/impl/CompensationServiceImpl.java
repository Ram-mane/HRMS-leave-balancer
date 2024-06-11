package com.hrms.app.service.impl;

import com.hrms.app.Enum.CompensationWorkStatus;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.responseDto.CompensationResponseDto;
import com.hrms.app.entity.CompensationWorkRequest;
import com.hrms.app.entity.Employee;
import com.hrms.app.mapper.CompensationMapper;
import com.hrms.app.repository.CompensationRepository;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.service.CompensationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class CompensationServiceImpl implements CompensationService {

    @Autowired
    EmpInfoRepository empInfoRepository;
    @Autowired
    CompensationRepository compensationRepository;

    @Override
    public CompensationResponseDto applyForCompensationWorkDay(String empEmail, LocalDate date) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        int weekendLength = employee.getOrganization().getPolicy().getWeekendLength();

        if (date.getDayOfWeek().getValue() <= (7-weekendLength) || !(date.isAfter(LocalDate.now())))
            throw new RuntimeException("Compensation work is only allowed on Weekends, Please choose" +
                    " a date accordingly");

        CompensationWorkRequest request = compensationRepository.findByEmpEmailAndRequestedForDate(empEmail, date);
        if (request != null)
            throw new RuntimeException("Cannot apply twice for the same date");

        CompensationWorkRequest compRequest = CompensationMapper.createCompensationRequest(date);

        compRequest.setEmployee(employee);
        employee.getCompensationRequestList().add(compRequest);

        empInfoRepository.save(employee);

        return CompensationMapper.compensationRequestToCompensationResponseDto(compRequest);
    }

    @Override
    public String approveOrRejectCompensationReq(UUID compensationRequestId, CompensationWorkStatus status, UUID organizationCode) {

        CompensationWorkRequest request = compensationRepository.findByCompensationRequestId(compensationRequestId);

        if(!request.getEmployee().getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        if (request.getRequestedForDate().isBefore(LocalDate.now()))
            throw new RuntimeException("Request approving period ended");

        request.setCompensationWorkStatus(status);

        compensationRepository.save(request);

        if(request.getCompensationWorkStatus().compareTo(CompensationWorkStatus.APPROVED) == 0)
            return "Request for compensation work day has been Approved";

        return "Request for compensation work day has been Rejected";
    }


    @Override
    public CompensationResponseDto getCompensationRequest(UUID compensationRequestId) {

        CompensationWorkRequest request = compensationRepository.findByCompensationRequestId(compensationRequestId);

        if (request == null)
            throw new RuntimeException("Invalid Compensation request id");

       return CompensationMapper.compensationRequestToCompensationResponseDto(request);
    }

    @Override
    public List<CompensationResponseDto> getCompensationWork(String empEmail, UUID organizationCode) {

        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if (employee == null)
            throw new RuntimeException("Invalid Employee email id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        List<CompensationWorkRequest> requestList = employee.getCompensationRequestList();

        List<CompensationResponseDto> responseDtoList = new ArrayList<>();

        for(CompensationWorkRequest request : requestList)
            responseDtoList.add(CompensationMapper.compensationRequestToCompensationResponseDto(request));

        return responseDtoList;
    }


/*    @Scheduled(cron = "0 0 12 * * SUN", zone = "Asia/Calcutta")
    public void recordCompensationDay() {

        List<CompensationWorkRequest> requestList = compensationRepository.findByRequestedForDate(LocalDate.now());

        for (CompensationWorkRequest request : requestList) {

            Employee employee = request.getEmployee();

            if (request.getCompensationWorkStatus().compareTo(CompensationWorkStatus.APPROVED) == 0 &&
                    employee.getAttendanceMarked()) {

                request.setCompensationWorkStatus(CompensationWorkStatus.FULFILLED);

                empInfoRepository.save(employee);
            }
        }
    }*/

}
