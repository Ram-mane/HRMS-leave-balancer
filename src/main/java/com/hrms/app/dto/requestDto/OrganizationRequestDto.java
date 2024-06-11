package com.hrms.app.dto.requestDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrganizationRequestDto {

    String pocEmail;

    String password;

    String pocNumber;

    String organizationName;

}
