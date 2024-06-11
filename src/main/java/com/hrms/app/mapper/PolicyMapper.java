package com.hrms.app.mapper;

import com.hrms.app.dto.requestDto.AddPolicyRequestDto;
import com.hrms.app.dto.requestDto.UpdatePolicyRequestDto;
import com.hrms.app.dto.responseDto.PolicyResponseDto;
import com.hrms.app.entity.Organization;
import com.hrms.app.entity.Policy;

import java.util.ArrayList;
import java.util.UUID;

public class PolicyMapper {

    public static Policy policyRequestDtoToPolicy(AddPolicyRequestDto addPolicyRequestDto) {
        return Policy.builder()
                .policyCode(UUID.randomUUID())
                .punchOutAllowed(addPolicyRequestDto.getPunchOutAllowed())
                .attendanceByManager(addPolicyRequestDto.getAttendanceByManager())
                .weekendLength(addPolicyRequestDto.getWeekendLength())
                .multipleShiftAllowed(addPolicyRequestDto.getMultipleShiftAllowed())
//                .shiftTiming(addPolicyRequestDto.getShiftTiming())
                .monthStartDay(addPolicyRequestDto.getMonthStartDay())
                .adjustCompensationWorkThroughSalary(addPolicyRequestDto.getAdjustCompensationWorkThroughSalary())
                .monthDurationInDays(addPolicyRequestDto.getMonthDurationInDays())
                .build();
    }

    public static PolicyResponseDto policyToPolicyResponseDto(Policy policy) {
        return PolicyResponseDto.builder()
                .attendanceByManager(policy.getAttendanceByManager())
                .adjustCompensationWorkThroughSalary(policy.getAdjustCompensationWorkThroughSalary())
                .monthDurationInDays(policy.getMonthDurationInDays())
                .monthStartDay(policy.getMonthStartDay())
                .weekendLength(policy.getWeekendLength())
                .multipleShiftAllowed(policy.getMultipleShiftAllowed())
                .organizationCode(policy.getOrganization().getOrganizationCode())
                .punchOutAllowed(policy.getPunchOutAllowed())
//                .shiftTiming(policy.getShiftTiming())
                .policyCode(policy.getPolicyCode())
                .build();
    }

    public static Policy updatePolicy(UpdatePolicyRequestDto updatePolicyRequestDto, Policy policy) {

        if(updatePolicyRequestDto.getAttendanceByManager() != null)
            policy.setAttendanceByManager(updatePolicyRequestDto.getAttendanceByManager());

        if(updatePolicyRequestDto.getMultipleShiftAllowed() != null)
            policy.setMultipleShiftAllowed(updatePolicyRequestDto.getMultipleShiftAllowed());

        if(updatePolicyRequestDto.getPunchOutAllowed() != null)
            policy.setPunchOutAllowed(updatePolicyRequestDto.getPunchOutAllowed());

        if (updatePolicyRequestDto.getShiftTiming() != null)
//            policy.setShiftTiming(updatePolicyRequestDto.getShiftTiming());

        if (updatePolicyRequestDto.getMonthStartDay() != null)
            policy.setMonthStartDay(updatePolicyRequestDto.getMonthStartDay());

        if (updatePolicyRequestDto.getMonthDurationInDays() != null)
            policy.setMonthDurationInDays(updatePolicyRequestDto.getMonthDurationInDays());

        if (updatePolicyRequestDto.getWeekendLength() != null)
            policy.setWeekendLength(updatePolicyRequestDto.getWeekendLength());

        if (updatePolicyRequestDto.getAdjustCompensationWorkThroughSalary() != null)
           policy.setAdjustCompensationWorkThroughSalary(updatePolicyRequestDto.getAdjustCompensationWorkThroughSalary());

        return policy;
    }
}
