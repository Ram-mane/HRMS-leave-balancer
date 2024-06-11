package com.hrms.app.dto.responseDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrganizationResponseDto {

    String pocEmail;

    String pocNumber;

    UUID organizationCode;

    String organizationName;

}
