package com.hrms.app.dto.requestDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GetAllEmployeesRequestDto {

    Integer pageNo;
    String sortBy;
    String order;
}
