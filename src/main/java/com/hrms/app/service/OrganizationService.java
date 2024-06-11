package com.hrms.app.service;

import com.hrms.app.dto.requestDto.OrganizationRequestDto;
import com.hrms.app.dto.responseDto.OrganizationResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface OrganizationService {

    public OrganizationResponseDto addOrganization(OrganizationRequestDto organizationRequestDto);

    public OrganizationResponseDto getOrganization(UUID organizationCode);

    public List<OrganizationResponseDto> getAllOrganization();
}
