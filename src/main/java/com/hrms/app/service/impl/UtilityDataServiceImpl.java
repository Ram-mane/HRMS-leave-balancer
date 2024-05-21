package com.hrms.app.service.impl;

import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.entity.UtilityData;
import com.hrms.app.repository.UtilityDataRepository;
import com.hrms.app.service.UtilityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UtilityDataServiceImpl implements UtilityDataService {

    @Autowired
    UtilityDataRepository utilityDataRepository;

    public String addUtilityTables() {
        Map<EmployeeType, Integer> monthlyLeaveAllocationMap = new HashMap<>();
        Map<LocalDate, String> nationalHolidays = new HashMap<>();
        Map<LocalDate, String> optionalHolidays = new HashMap<>();
        UtilityData utilityData = new UtilityData(1, monthlyLeaveAllocationMap, nationalHolidays, optionalHolidays);

        utilityDataRepository.save(utilityData);

        return "Utility Tables created";
    }

    public UtilityData getUtilityData() {
        Optional<UtilityData> optionalTables = utilityDataRepository.findById(1);

        if(optionalTables.isEmpty()) {
            addUtilityTables();
            optionalTables = utilityDataRepository.findById(1);
        }

        UtilityData utilityData = optionalTables.get();

        if (utilityData == null) {
            throw new RuntimeException("Utility Table not found");
        }

        return utilityData;
    }

    public String addEmployeeType(EmployeeType employeeType, int noOfCasualLeave) {

        UtilityData utilityData = getUtilityData();
        Map<EmployeeType, Integer> map = utilityData.getMonthlyLeaveAllocationMap();

        map.put(employeeType, noOfCasualLeave);

        utilityDataRepository.save(utilityData);

        return "Employee Type " + employeeType + " with " + noOfCasualLeave + " casual leaves added";

    }

    public String updateEmployeeType(EmployeeType employeeType, int noOfCasualLeave) {

        return addEmployeeType(employeeType, noOfCasualLeave);

    }

    public String removeEmployeeType(EmployeeType employeeType) {

        UtilityData utilityData = getUtilityData();
        Map<EmployeeType, Integer> map = utilityData.getMonthlyLeaveAllocationMap();

        if(map.containsKey(employeeType))
            map.remove(employeeType);

        utilityDataRepository.save(utilityData);

        return "Employee Type " + employeeType + " remove successfully";

    }

    public String addNationalHoliday(String event, LocalDate date) {

        UtilityData utilityData = getUtilityData();
        Map<LocalDate, String> map = utilityData.getNationalHolidays();

        map.put(date, event);

        utilityDataRepository.save(utilityData);

        return "National holiday " + event + " on " + date + " added";

    }

    public String updateNationalHoliday(String event, LocalDate date) {

        return addNationalHoliday(event, date);

    }

    public String removeNationalHoliday(String event) {

        UtilityData utilityData = getUtilityData();
        Map<LocalDate, String> map = utilityData.getNationalHolidays();

        for(LocalDate date : map.keySet()) {
            if(map.get(date).equals(event)) {
                map.remove(date);
                utilityDataRepository.save(utilityData);
                return "National holiday " + event + " removed successfully";
            }
        }

        return "No event with name : " + event + " found";

    }

    public String addOptionalHoliday(String event, LocalDate date) {

        UtilityData utilityData = getUtilityData();
        Map<LocalDate, String> map = utilityData.getOptionalHolidays();

        map.put(date, event);

        utilityDataRepository.save(utilityData);

        return "Optional holiday " + event + " on " + date + " added";

    }

    public String updateOptionalHoliday(String event, LocalDate date) {

        return addOptionalHoliday(event, date);

    }

    public String removeOptionalHoliday(String event) {

        UtilityData utilityData = getUtilityData();
        Map<LocalDate, String> map = utilityData.getOptionalHolidays();

        for(LocalDate date : map.keySet()) {
            if(map.get(date).equals(event)) {
                map.remove(date);
                utilityDataRepository.save(utilityData);
                return "Optional holiday " + event + " removed successfully";
            }
        }

        return "No event with name : " + event + " found";

    }

}
