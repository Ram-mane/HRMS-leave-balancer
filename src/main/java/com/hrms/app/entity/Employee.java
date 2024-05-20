package com.hrms.app.entity;


import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "employee")
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID empId;

    String empName;

    @Column(unique = true)
    String empEmail;

    String empPassword;

    @Column(unique = true)
    String empPhone;

    @Enumerated(EnumType.STRING)
    EmployeeType empType;

    @Enumerated(EnumType.STRING)
    Designation empDesignation;

    String imgUrl;

    int empSalary;

    LocalDate dateOfBirth;

    LocalDate joiningDate;

    LocalDate lastFlexiLeaveTaken;

    boolean activeEmployee;

    int casualLeavesLeft;
    int optionalLeavesLeft;
    int flexiLeavesLeft;
    int nationalLeavesLeft;
    int personalLeavesLeft;

    int noOfCompensationWorkDayLeft;

    List<LocalDate> compensationWorkDayList;
    List<LocalDate> dateList;
    List<LeaveStatus> statusList;

    int leaveCredited;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<Leave> leaveList;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<Attendance> attendanceList;

    boolean attendanceMarked;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime modifiedAt;

    String createdBy;
    String modifiedBy;

}
