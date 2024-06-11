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
    Double casual_leaves_left;
    Integer optional_leaves_left;
    Integer flexi_leaves_left;
    Integer national_leaves_left;
    Integer personal_leaves_left;
}
