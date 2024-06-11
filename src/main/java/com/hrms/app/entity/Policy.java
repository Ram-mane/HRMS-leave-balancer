package com.hrms.app.entity;

import com.hrms.app.Enum.EmployeeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.MonthDay;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "policies")
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    UUID policyCode;


    //Organization details

    @OneToOne
    @JoinColumn
    Organization organization;

    //Attendance related data
    Boolean punchOutAllowed;

    Boolean attendanceByManager;

    Integer weekendLength;


    //Shift
    Boolean multipleShiftAllowed;

//    List<List<LocalTime>> shiftTiming;


    //salary related data
    Integer monthDurationInDays; // 26 or 30

    Boolean adjustCompensationWorkThroughSalary; // True- add in salary, False- adjust in casual leaves

    Integer monthStartDay; //same to be used as salary Calculation day

}
