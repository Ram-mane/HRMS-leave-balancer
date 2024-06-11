package com.hrms.app.dto.responseDto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SalaryResponseDto {

    String empName;
    String empEmail;
    Integer salaryToBePaid;
    Integer fixedSalary;
    Integer NoOfDayAccountedFor;
    Integer NoOfLeavesTaken;

}
