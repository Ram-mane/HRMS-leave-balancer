package com.hrms.app.dto.requestDto;

import com.hrms.app.entity.Designation;
//import com.hrms.app.Enum.Designation;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.entity.Designation;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

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
    MultipartFile empImage;
    Integer empSalary;
    UUID organizationCode;
    LocalDate dateOfBirth;
    LocalDate joiningDate;
    Integer shiftNumber;

}
