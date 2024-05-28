package com.hrms.app.service;

import com.hrms.app.Enum.EmployeeType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public interface UtilityDataService {

    String addUtilityTables();
    String addEmployeeType(EmployeeType employeeType, int noOfCasualLeave);

    String updateEmployeeType(EmployeeType employeeType, int noOfCasualLeave);

    String removeEmployeeType(EmployeeType employeeType);

    String addNationalHoliday(String event, LocalDate date);

    String updateNationalHoliday(String event, LocalDate date);

    String removeNationalHoliday(String event);

    String addOptionalHoliday(String event, LocalDate date);

    String updateOptionalHoliday(String event, LocalDate date);

    String removeOptionalHoliday(String event);

    Map<EmployeeType, Integer> getEmployeeType();

    Map<LocalDate, String> getNationalHolidays();

    Map<LocalDate, String> getOptionalHolidays();
}
