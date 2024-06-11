package com.hrms.app.dto.responseDto;

import com.hrms.app.Enum.EmployeeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LeavePolicyResponseDto {

    UUID LeavePolicyCode;

    UUID organizationCode;

    String organizationName;

    //Casual leave map
    Map<EmployeeType, Integer> casualLeaveMap;

    Boolean personalLeaveAllowed;

    Boolean flexiLeaveAllowed;
    Integer noOfMonthsBetweenTwoFlexiLeave;

    Boolean optionalLeaveAllowed;
    Integer noOfOptionalLeavesAllotted;
    List<MonthDay> optionalLeaves;

    List<MonthDay> nationalLeaves;

    //miscellaneous leaves
    //Event name, duration in days
    Map<String, Integer> durationSpecificLeaveList;

    //Date related miscellaneous leaves
    Map<String, LocalDate> dateSpecificLeaveList;

}
