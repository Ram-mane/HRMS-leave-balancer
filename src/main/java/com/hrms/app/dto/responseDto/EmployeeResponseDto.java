package com.hrms.app.dto.responseDto;

import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmployeeResponseDto {

    String empName;
    String empEmail;
    String empPhone;
    EmployeeType empType;
    Designation empDesignation;
    String imgUrl;
    LocalDate joiningDate;
    LocalDate dateOfBirth;
    int empSalary;
    boolean activeEmployee;

}
