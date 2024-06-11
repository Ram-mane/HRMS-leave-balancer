package com.hrms.app.dto.requestDto;

import com.hrms.app.Enum.EmployeeType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

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
public class LeavePolicyRequestDto {

    UUID organizationCode;

    //Casual leave map
    Map<EmployeeType, Integer> casualLeaveMap;

    Boolean personalLeaveAllowed;

    Boolean flexiLeaveAllowed;
    Integer noOfMonthsBetweenTwoFlexiLeave;

    Boolean optionalLeaveAllowed;
    Integer noOfOptionalLeavesAllotted;

    @DateTimeFormat(pattern = "MM-dd")
    List<MonthDay> optionalLeaves;

    @DateTimeFormat(pattern = "MM-dd")
    List<MonthDay> nationalLeaves;

    //miscellaneous leaves
    //Event name, duration in days
    Map<String, Integer> durationSpecificLeaveList;

    //Date related miscellaneous leaves
    Map<String, LocalDate> dateSpecificLeaveList;

}
