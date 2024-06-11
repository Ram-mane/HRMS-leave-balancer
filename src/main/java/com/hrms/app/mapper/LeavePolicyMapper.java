package com.hrms.app.mapper;

import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.dto.requestDto.LeavePolicyRequestDto;
import com.hrms.app.dto.responseDto.LeavePolicyResponseDto;
import com.hrms.app.entity.LeavePolicy;

import java.time.LocalDate;
import java.util.*;

public class LeavePolicyMapper {

    public static LeavePolicy leavePolicyRequestDtoToLeavePolicy(LeavePolicyRequestDto leavePolicyRequestDto) {

        LeavePolicy leavePolicy = LeavePolicy.builder()
                .LeavePolicyCode(UUID.randomUUID())
                .personalLeaveAllowed(leavePolicyRequestDto.getPersonalLeaveAllowed())
                .flexiLeaveAllowed(leavePolicyRequestDto.getFlexiLeaveAllowed())
                .optionalLeaveAllowed(leavePolicyRequestDto.getOptionalLeaveAllowed())
//                .nationalLeaves(leavePolicyRequestDto.getNationalLeaves())
                .build();

        List<EmployeeType> employeeTypes = new ArrayList<>();
        List<Integer> casualLeavesAllotted = new ArrayList<>();
        Map<EmployeeType, Integer> map = leavePolicyRequestDto.getCasualLeaveMap();
        for (EmployeeType type : map.keySet()) {
            employeeTypes.add(type);
            casualLeavesAllotted.add(employeeTypes.indexOf(type), map.get(type));
        }
        leavePolicy.setEmployeeTypes(employeeTypes);
        leavePolicy.setCasualLeavesAllotted(casualLeavesAllotted);

        if(leavePolicyRequestDto.getFlexiLeaveAllowed()) {
            if(leavePolicyRequestDto.getNoOfMonthsBetweenTwoFlexiLeave() == null)
                leavePolicy.setNoOfMonthsBetweenTwoFlexiLeave(4);
            leavePolicy.setNoOfMonthsBetweenTwoFlexiLeave(leavePolicyRequestDto.getNoOfMonthsBetweenTwoFlexiLeave());
        }

        if(leavePolicyRequestDto.getOptionalLeaveAllowed()) {
            leavePolicy.setNoOfOptionalLeavesAllotted(leavePolicyRequestDto.getNoOfOptionalLeavesAllotted());
//            leavePolicy.setOptionalLeaves(leavePolicyRequestDto.getOptionalLeaves());
        }

        List<String> durationSpecificleaveList = new ArrayList<>();
        List<Integer> durationInDays = new ArrayList<>();
        if(leavePolicyRequestDto.getDurationSpecificLeaveList() != null) {
            Map<String, Integer> leaveMap = leavePolicyRequestDto.getDurationSpecificLeaveList();
            for (String key : leaveMap.keySet()) {
                durationSpecificleaveList.add(key);
                durationInDays.add(durationSpecificleaveList.indexOf(key), leaveMap.get(key));
            }

            leavePolicy.setDurationSpecificLeaveList(durationSpecificleaveList);
            leavePolicy.setDurationInDays(durationInDays);
        }

        List<String> dateSpecificleaveList = new ArrayList<>();
        List<LocalDate> specificDateList = new ArrayList<>();
        if(leavePolicyRequestDto.getDateSpecificLeaveList() != null) {
            Map<String, LocalDate> leaveMap = leavePolicyRequestDto.getDateSpecificLeaveList();
            for (String key : leaveMap.keySet()) {
                dateSpecificleaveList.add(key);
                specificDateList.add(dateSpecificleaveList.indexOf(key), leaveMap.get(key));
            }

            leavePolicy.setDateSpecificLeaveList(dateSpecificleaveList);
            leavePolicy.setSpecificDateList(specificDateList);
        }

        return leavePolicy;
    }

    public static LeavePolicyResponseDto leavePolicyToLeavePolicyResponseDto(LeavePolicy leavePolicy) {

        LeavePolicyResponseDto leavePolicyResponseDto = LeavePolicyResponseDto.builder()
                .LeavePolicyCode(leavePolicy.getLeavePolicyCode())
                .organizationCode(leavePolicy.getOrganization().getOrganizationCode())
                .organizationName(leavePolicy.getOrganization().getOrganizationName())
                .personalLeaveAllowed(leavePolicy.getPersonalLeaveAllowed())
                .flexiLeaveAllowed(leavePolicy.getFlexiLeaveAllowed())
//                .optionalLeaves(leavePolicy.getOptionalLeaves())
//                .nationalLeaves(leavePolicy.getNationalLeaves())
                .build();

        List<EmployeeType> employeeTypes = leavePolicy.getEmployeeTypes();
        List<Integer> casualLeavesAllotted = leavePolicy.getCasualLeavesAllotted();
        Map<EmployeeType, Integer> map = new HashMap<>();
        for (EmployeeType type : employeeTypes) {
            map.put(type, casualLeavesAllotted.get(employeeTypes.indexOf(type)));
        }
        leavePolicyResponseDto.setCasualLeaveMap(map);

        if(leavePolicy.getFlexiLeaveAllowed())
            leavePolicyResponseDto.setNoOfMonthsBetweenTwoFlexiLeave(leavePolicy.getNoOfMonthsBetweenTwoFlexiLeave());

        if(leavePolicy.getOptionalLeaveAllowed()) {
            leavePolicyResponseDto.setNoOfOptionalLeavesAllotted(leavePolicy.getNoOfOptionalLeavesAllotted());
//            leavePolicyResponseDto.setOptionalLeaves(leavePolicy.getOptionalLeaves());
        }

        if(leavePolicy.getDurationSpecificLeaveList() != null) {
            List<String> durationSpecificleaveList = leavePolicy.getDurationSpecificLeaveList();
            List<Integer> durationInDays = leavePolicy.getDurationInDays();

            Map<String, Integer> leaveMap = new HashMap<>();
            for (String key : durationSpecificleaveList) {
                leaveMap.put(key, durationInDays.get(durationSpecificleaveList.indexOf(key)));
            }

            leavePolicyResponseDto.setDurationSpecificLeaveList(leaveMap);
        }

        if(leavePolicy.getDateSpecificLeaveList() != null) {
            List<String> dateSpecificleaveList = leavePolicy.getDateSpecificLeaveList();
            List<LocalDate> specifixDateList = leavePolicy.getSpecificDateList();

            Map<String, LocalDate> leaveMap = new HashMap<>();
            for (String key : dateSpecificleaveList) {
                leaveMap.put(key, specifixDateList.get(dateSpecificleaveList.indexOf(key)));
            }

            leavePolicyResponseDto.setDateSpecificLeaveList(leaveMap);
        }

        return leavePolicyResponseDto;
    }

    public static LeavePolicy updateLeavePolicy(LeavePolicy leavePolicy, LeavePolicyRequestDto requestDto) {

        if (requestDto.getCasualLeaveMap() != null) {
            List<EmployeeType> employeeTypes = leavePolicy.getEmployeeTypes();
            List<Integer> casualLeavesAllotted = leavePolicy.getCasualLeavesAllotted();
            Map<EmployeeType, Integer> map = requestDto.getCasualLeaveMap();
            for (EmployeeType type : map.keySet()) {
                if(!employeeTypes.contains(type))
                    employeeTypes.add(type);
                casualLeavesAllotted.add(employeeTypes.indexOf(type), map.get(type));
            }
            leavePolicy.setEmployeeTypes(employeeTypes);
            leavePolicy.setCasualLeavesAllotted(casualLeavesAllotted);

        }

        if(requestDto.getPersonalLeaveAllowed() != null) {
            leavePolicy.setPersonalLeaveAllowed(requestDto.getPersonalLeaveAllowed());
        }

        if(requestDto.getFlexiLeaveAllowed() != null) {
            leavePolicy.setFlexiLeaveAllowed(requestDto.getFlexiLeaveAllowed());
            if(requestDto.getFlexiLeaveAllowed())
                leavePolicy.setNoOfMonthsBetweenTwoFlexiLeave(requestDto.getNoOfMonthsBetweenTwoFlexiLeave());
        }

        if(requestDto.getOptionalLeaveAllowed() != null) {
            leavePolicy.setOptionalLeaveAllowed(requestDto.getOptionalLeaveAllowed());
            if(requestDto.getOptionalLeaveAllowed()) {
                leavePolicy.setNoOfOptionalLeavesAllotted(requestDto.getNoOfOptionalLeavesAllotted());
//                leavePolicy.setOptionalLeaves(requestDto.getOptionalLeaves());
            }
        }

        if(requestDto.getNationalLeaves() != null) {
//            leavePolicy.setNationalLeaves(requestDto.getNationalLeaves());
        }

        if(requestDto.getDurationSpecificLeaveList() != null) {
            List<String> durationSpecificleaveList = new ArrayList<>();
            List<Integer> durationInDays = new ArrayList<>();

            Map<String, Integer> leaveMap = requestDto.getDurationSpecificLeaveList();
            for (String key : leaveMap.keySet()) {
                durationSpecificleaveList.add(key);
                durationInDays.add(durationSpecificleaveList.indexOf(key), leaveMap.get(key));
            }

            leavePolicy.setDurationSpecificLeaveList(durationSpecificleaveList);
            leavePolicy.setDurationInDays(durationInDays);
        }

        if(requestDto.getDateSpecificLeaveList() != null) {
            List<String> dateSpecificleaveList = new ArrayList<>();
            List<LocalDate> specificDateList = new ArrayList<>();

            Map<String, LocalDate> leaveMap = requestDto.getDateSpecificLeaveList();
            for (String key : leaveMap.keySet()) {
                dateSpecificleaveList.add(key);
                specificDateList.add(dateSpecificleaveList.indexOf(key), leaveMap.get(key));
            }

            leavePolicy.setDateSpecificLeaveList(dateSpecificleaveList);
            leavePolicy.setSpecificDateList(specificDateList);
        }

        return leavePolicy;

    }

}
