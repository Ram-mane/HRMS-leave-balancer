package com.hrms.app.dto.responseDto;

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
public class EmployeeLeaveResponseDto {
    String empName;
    String empEmail;
    String empPhone;
    EmployeeType empType;
    String empDesignation;
    String imgUrl;
    double casual_leaves_left;
    int optional_leaves_left;
    int flexi_leaves_left;
    int national_leaves_left;
    int personal_leaves_left;
}
