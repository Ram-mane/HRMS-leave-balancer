package com.hrms.app.dto.requestDto;

import com.hrms.app.entity.Designation;
//import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String empDesignation;
    String imgUrl;
    int empSalary;

}
