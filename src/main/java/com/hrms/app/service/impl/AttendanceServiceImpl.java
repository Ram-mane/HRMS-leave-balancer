package com.hrms.app.service.impl;

import com.hrms.app.Enum.AttendanceStatus;
import com.hrms.app.dto.requestDto.AttendanceRequestDto;
import com.hrms.app.dto.responseDto.AttendanceResponseDto;
import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Employee;
import com.hrms.app.mapper.AttendanceMapper;
import com.hrms.app.repository.AttendanceRepository;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    EmpInfoRepository empInfoRepository;

    @Override
    public AttendanceResponseDto markAttendance(AttendanceRequestDto attendanceRequestDto) {

        Employee employee = empInfoRepository.findByEmpEmail(attendanceRequestDto.getEmpEmail());

        if(employee == null) {
            throw new RuntimeException("Invalid Employee Email Id");
        }

        if(!employee.isActiveEmployee()) {
            throw new RuntimeException("Sorry, Your don't have the necessary access privilege");
        }

        if(employee.isAttendanceMarked()) {
            throw new RuntimeException("Your attendance is already recorded for today");
        }

        Attendance attendance = AttendanceMapper.AttendanceRequestDtoToAttendance(attendanceRequestDto);

        attendance.setEmployee(employee);

        employee.getAttendanceList().add(attendance);

        empInfoRepository.save(employee);

        return AttendanceMapper.AttendanceToAttendanceResponseDto(attendance);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Calcutta")
    public void resetAttendanceMarked() {

        List<Employee> employeeList = empInfoRepository.findAll();

        if(employeeList.isEmpty()) {
            return;
        }

        for (Employee employee : employeeList) {
            employee.setAttendanceMarked(false);
            empInfoRepository.save(employee);
        }

    }

    @Scheduled(cron = "0 0 12 * * MON-SAT", zone = "Asia/Calcutta")
    public void recordEmployeesOnLeave() {

        List<Employee> employeeList = empInfoRepository.getAbsentEmployeeList();

        for (Employee employee : employeeList) {

            Attendance attendance = new Attendance(AttendanceStatus.NOT_MARKED, employee);
            employee.getAttendanceList().add(attendance);
            employee.setAttendanceMarked(true);
            empInfoRepository.save(employee);

        }
    }

    @Scheduled(cron = "0 0 12 * * SUN", zone = "Asia/Calcutta")
    public void recordCompensationDay() {

        List<Employee> employeeList = empInfoRepository.getPresentEmployeeList();

        for (Employee employee : employeeList) {
            if (employee.getNoOfCompensationWorkDayLeft() > 0) {

                employee.setNoOfCompensationWorkDayLeft(employee.getNoOfCompensationWorkDayLeft() - 1);
                employee.getCompensationWorkDayList().add(LocalDate.now());

                empInfoRepository.save(employee);
            }
        }
    }

}
