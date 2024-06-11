package com.hrms.app.service;

import com.hrms.app.Enum.AttendanceStatus;
import com.hrms.app.Enum.CompensationWorkStatus;
import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.CompensationWorkRequest;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Organization;
import com.hrms.app.repository.CompensationRepository;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ScheduledTaskService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    EmpInfoRepository empInfoRepository;

    @Autowired
    CompensationRepository compensationRepository;

    public void scheduledTask() {

        List<Organization> organizationList = organizationRepository.findAll();

        for(Organization organization : organizationList) {
            System.out.println(organization.getOrganizationName());
            if(organization.getPolicy() == null) {
                System.out.println("no_policy--------");
                continue;
            }

//            List<List<LocalTime>> shiftList = organization.getPolicy().getShiftTiming();
//
//            for (int i = 0; i < shiftList.size(); i++) {
//
//                List<LocalTime> shift = shiftList.get(i);
//                LocalTime shiftStartTime = shift.get(0);
//
//                resetAttendance(shiftStartTime.minusHours(2), organization, i);
//
//                closeAttendance(shiftStartTime.plusHours(2), organization, i);
//
//            }

        }
    }

    public void resetAttendance(LocalTime localTime, Organization organization, Integer shiftNo) {
        System.out.println("2");
        long initialDelay = Duration.between(LocalTime.now(), localTime).getSeconds();
        if (initialDelay < 0) {
            // If the start time is in the past, adjust it to tomorrow
            initialDelay += Duration.ofDays(1).getSeconds();
        }

        // Create a scheduled executor service
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // Schedule the task
        long finalInitialDelay = initialDelay*1000;
        executor.submit(() -> {
            try {
                // Sleep for the calculated delay
                Thread.sleep(finalInitialDelay);

//                List<Employee> employeeList = organizationRepository.findEmployeesByOrganizationCodeAndShiftNo(
//                        organization.getOrganizationCode(), shiftNo);
//                for (Employee employee : employeeList) {
//                    employee.setAttendanceMarked(false);
//                    empInfoRepository.save(employee);
//                    System.out.println("reset------" + employee.getEmpName());
//                }
                empInfoRepository.updateAttendanceMarkedForOrganizationAndShift(organization.getOrganizationCode(), shiftNo);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
    }

    public void closeAttendance(LocalTime localTime, Organization organization, Integer shiftNo) {
        System.out.println("1");

        long initialDelay = Duration.between(LocalTime.now(), localTime).getSeconds();
        if (initialDelay < 0) {
            // If the start time is in the past, adjust it to tomorrow
            initialDelay += Duration.ofDays(1).getSeconds();
        }

        // Create a scheduled executor service
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // Schedule the task
        long finalInitialDelay = initialDelay*1000;
        executor.submit(() -> {
            try {
                // Sleep for the calculated delay
                Thread.sleep(finalInitialDelay);

                //Cheking for weekends
//                if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.SUNDAY))
//
//                if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
//                     organization.getPolicy().getWeekendLength() == 2)

                int weekendLength = organization.getPolicy().getWeekendLength();
                if(LocalDate.now().getDayOfWeek().getValue() > (7-weekendLength)) {

                    //checking for compensating employees
                    List<CompensationWorkRequest> requestList = compensationRepository.findByRequestedForDate(LocalDate.now());

                    for (CompensationWorkRequest request : requestList) {

                        Employee employee = request.getEmployee();

                        if (request.getCompensationWorkStatus().compareTo(CompensationWorkStatus.APPROVED) == 0 &&
                                employee.getAttendanceMarked()) {

                            request.setCompensationWorkStatus(CompensationWorkStatus.FULFILLED);

                            if(!organization.getPolicy().getAdjustCompensationWorkThroughSalary())
                                employee.setCasualLeavesLeft(employee.getCasualLeavesLeft()+1);

                            empInfoRepository.save(employee);
                        }
                    }
                    return;
                }

                List<Employee> employeeList = organizationRepository.findEmployeesByOrganizationCodeAndShiftNo(
                        organization.getOrganizationCode(), shiftNo);
                for (Employee employee : employeeList) {

                    if(!employee.getAttendanceMarked()) {

                        System.out.println(employee.getEmpName());
                        Attendance attendance = new Attendance(AttendanceStatus.NOT_MARKED, employee);

                        Employee currEmployee = empInfoRepository.getEmployeeAndAttendanceListByEmpEmail(employee.getEmpEmail());
                        currEmployee.getAttendanceList().add(attendance);

                        currEmployee.setAttendanceMarked(true);

                        empInfoRepository.save(currEmployee);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        executor.shutdown();
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Calcutta")
    public void runScheduledTask() {

        scheduledTask();
        System.out.println("started----------");

    }

    /* @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Calcutta")
    public void resetAttendanceMarked() {

        List<Employee> employeeList = empInfoRepository.findAll();

        if(employeeList.isEmpty()) {
            return;
        }

        for (Employee employee : employeeList) {//query to reset column
            employee.setAttendanceMarked(false);
            empInfoRepository.save(employee);
        }

    }*/

    /*
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
*/

}
