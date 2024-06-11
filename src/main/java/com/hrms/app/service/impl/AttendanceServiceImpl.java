package com.hrms.app.service.impl;

import com.hrms.app.Enum.AttendanceFrom;
import com.hrms.app.Enum.AttendanceStatus;
import com.hrms.app.Security.JwtTokenHelper;
import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.requestDto.ChangeAttendanceRequestDto;
import com.hrms.app.dto.requestDto.GetAttendanceRequestDto;
import com.hrms.app.dto.responseDto.AddAttendanceResponseDto;
import com.hrms.app.dto.responseDto.GetAttendanceResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Employee;
import com.hrms.app.mapper.AttendanceMapper;
import com.hrms.app.repository.AttendanceRepository;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.OrganizationRepository;
import com.hrms.app.service.AttendanceService;
import com.hrms.app.service.PaginationService;
import com.hrms.app.service.TokenServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    EmpInfoRepository empInfoRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenServiceHelper tokenServiceHelper;

    @Autowired
    PaginationService paginationService;

    @Override
    public AddAttendanceResponseDto markAttendance(AttendanceRequestDto attendanceRequestDto) {

        Employee employee = empInfoRepository.findByEmpEmail(attendanceRequestDto.getEmpEmail());

        if(employee == null)
            throw new RuntimeException("Invalid Employee Email Id");

        //Role based authentication according to policy

        if(employee.getOrganization().getPolicy().getAttendanceByManager()) {
            String token = tokenServiceHelper.extractJwtToken();
            String username = jwtTokenHelper.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(!userDetails.getAuthorities().contains("LPT_ADMIN") && !userDetails.getAuthorities().contains("ORG_ADMIN"))
                throw new RuntimeException("Access denied");

            if(organizationRepository.findByUsername(username) == null)
                throw new RuntimeException("Access denied");
        }

        if(employee.getAttendanceMarked())
            throw new RuntimeException("Your attendance is already recorded for today");

        Attendance attendance = AttendanceMapper.AttendanceRequestDtoToAttendance(attendanceRequestDto);

        attendance.setEmployee(employee);

        employee.getAttendanceList().add(attendance);

        employee.setAttendanceMarked(true);

        empInfoRepository.save(employee);

        return AttendanceMapper.AttendanceToAddAttendanceResponseDto(attendance);
    }

    @Override
    public PageResponseDto getAttendanceList(GetAttendanceRequestDto getAttendanceRequestDto, UUID organizationCode) throws Exception {

        int pageNo = getAttendanceRequestDto.getPageNo() != null ? getAttendanceRequestDto.getPageNo() : 0;

        String empEmail = getAttendanceRequestDto.getEmpEmail();
        Employee employee = empInfoRepository.findByEmpEmail(empEmail);

        if(employee == null)
            throw new RuntimeException("Invalid Employee Email Id");

        if(!employee.getOrganizationCode().equals(organizationCode))
            throw new RuntimeException("Organization code did not matched with employees organization");

        List<Attendance> attendanceList = new ArrayList<>();

        if(getAttendanceRequestDto.getAttendanceFrom() != null) {
            LocalDate date = LocalDate.now();
            if(getAttendanceRequestDto.getAttendanceFrom().equals(AttendanceFrom.LAST_WEEK))
                date = date.minusWeeks(1);

            else if(getAttendanceRequestDto.getAttendanceFrom().equals(AttendanceFrom.LAST_MONTH))
                date = date.minusMonths(1);

            else if(getAttendanceRequestDto.getAttendanceFrom().equals(AttendanceFrom.LAST_YEAR))
                date = date.minusYears(1);

            attendanceList = attendanceRepository.findAttentionAfterDate(empEmail, date);
        }
        else if (getAttendanceRequestDto.getFromDate() != null) {
            LocalDate fromDate = getAttendanceRequestDto.getFromDate();
            if(getAttendanceRequestDto.getTillDate() != null) {
                attendanceList = attendanceRepository.findAttentionBetweenDate(empEmail,
                        fromDate, getAttendanceRequestDto.getTillDate());
            }
            else
                attendanceList = attendanceRepository.findAttentionAfterDate(empEmail, fromDate);
        }

        return paginationService.paginationOnAttendanceList(pageNo, attendanceList);

    }

    @Override
    public PageResponseDto getAttendanceListOfADay(int pageNo, LocalDate date, UUID organizationCode) throws Exception {

        List<Attendance> attendanceList = attendanceRepository.findByDate(date);

        if(attendanceList.isEmpty())
            throw new RuntimeException("No attendance data for date "+ date +" found");

        return paginationService.paginationOnAttendanceList(pageNo, attendanceList);
    }

    @Override
    public GetAttendanceResponseDto punchOut(String empEmail) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findByEmployeeEmailAndDate(empEmail, LocalDate.now());

        if(optionalAttendance.isEmpty())
            throw new RuntimeException("You didn't punch in today");

        Attendance attendance = optionalAttendance.get();

        if(!attendance.getEmployee().getOrganization().getPolicy().getPunchOutAllowed())
            throw new RuntimeException("Access denied");

//        if(!attendance.getEmployee().getAttendanceMarked())
//            throw new RuntimeException("Your have to punch in first!!");

        if(attendance.getAttendanceStatus().equals(AttendanceStatus.ON_LEAVE))
            throw new RuntimeException("Invalid request, you are marked for leave today");

        if(attendance.getPunchOutTime() != null)
            throw new RuntimeException("Your punch out time for today is already recorded");

        attendance.setPunchOutTime(LocalTime.now());

        attendance.setActiveTime(ChronoUnit.MINUTES.between(attendance.getPunchInTime(), attendance.getPunchOutTime()) / 60.0);

        attendanceRepository.save(attendance);

        return AttendanceMapper.AttendanceToGetAttendanceResponseDto(attendance);
    }

    @Override
    public GetAttendanceResponseDto attendanceCorrection(ChangeAttendanceRequestDto requestDto) {

        if(requestDto == null)
            throw new RuntimeException("Request dto cannot be null");

        Employee employee = empInfoRepository.findByEmpEmail(requestDto.getEmpEmail());

        if(employee == null)
            throw new RuntimeException("Invalid Employee Email Id");

        Optional<Attendance> optionalAttendance = attendanceRepository.findByEmployeeEmailAndDate(requestDto.getEmpEmail(), requestDto.getDate());

        Attendance attendance = optionalAttendance.orElseGet(() -> AttendanceMapper.ChangeAttentionRequestDtoToAttention(requestDto));

        attendance.setEmployee(employee);

        if (!employee.getAttendanceList().contains(attendance))
            employee.getAttendanceList().add(attendance);

        empInfoRepository.save(employee);

        return AttendanceMapper.AttendanceToGetAttendanceResponseDto(attendance);
    }

}
