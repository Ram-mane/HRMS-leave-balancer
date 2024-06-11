package com.hrms.app.entity;

import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "leave_policy")
@Builder
public class LeavePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    UUID LeavePolicyCode;

    @OneToOne
    @JoinColumn
    Organization organization;

    //Casual leave table
    List<EmployeeType> employeeTypes;
    List<Integer> casualLeavesAllotted;

    //personal leaves
    //only 2 are allowed, 1 for DOB 2 for work anniversary (Joining date)
    Boolean personalLeaveAllowed;

    //flex leaves
    Boolean flexiLeaveAllowed;
    Integer noOfMonthsBetweenTwoFlexiLeave;// Ex. one in 4 months span

    //Optional leaves
    Boolean optionalLeaveAllowed;
    Integer noOfOptionalLeavesAllotted;
    List<LocalDate> optionalLeaves;

    //National leaves
    List<LocalDate> nationalLeaves;

    //miscellaneous leaves
    List<String> durationSpecificLeaveList;
    List<Integer> durationInDays;

    //Date related miscellaneous leaves
    List<String> dateSpecificLeaveList;
    List<LocalDate> specificDateList;

}
