package com.hrms.app.dto.requestDto;


import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmployeeUpdateRequestDto {

    String empEmail;
    String empName;
    String empPassword;
    String empPhone;
    EmployeeType empType;
    Designation empDesignation;
    String imgUrl;
    int empSalary;

}
