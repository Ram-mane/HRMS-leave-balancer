package com.hrms.app.mapper;

import com.hrms.app.dto.requestDto.OrganizationRequestDto;
import com.hrms.app.dto.responseDto.OrganizationResponseDto;
import com.hrms.app.entity.Organization;

import java.util.ArrayList;
import java.util.UUID;

public class OrganizationMapper {

    public static Organization organizationRequestDtoToOrganization(OrganizationRequestDto organizationRequestDto) {
        return Organization.builder()
                .organizationCode(UUID.randomUUID())
                .pocEmail(organizationRequestDto.getPocEmail())
                .password(organizationRequestDto.getPassword())
                .pocNumber(organizationRequestDto.getPocNumber())
                .organizationName(organizationRequestDto.getOrganizationName())
                .employeeList(new ArrayList<>())
                .build();
    }

    public static OrganizationResponseDto organizationToOrganizationResponseDto(Organization organization) {
        return OrganizationResponseDto.builder()
                .organizationCode(organization.getOrganizationCode())
                .organizationName(organization.getOrganizationName())
                .pocEmail(organization.getPocEmail())
                .pocNumber(organization.getPocNumber())
                .build();
    }
}
