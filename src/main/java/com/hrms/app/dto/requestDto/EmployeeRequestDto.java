package com.hrms.app.dto.requestDto;

import com.hrms.app.entity.Designation;
//import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.entity.Designation;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmployeeRequestDto {

    String empName;
    String empEmail;
    String empPassword;
    String empPhone;
    EmployeeType empType;
    String empDesignation;
    String imgUrl;
    int empSalary;
    LocalDate dateOfBirth;
    LocalDate joiningDate;

}
