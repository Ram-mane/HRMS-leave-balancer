package com.hrms.app.service.impl;

import com.hrms.app.dto.requestDto.OrganizationRequestDto;
import com.hrms.app.dto.responseDto.OrganizationResponseDto;
import com.hrms.app.entity.Organization;
import com.hrms.app.entity.UserRole;
import com.hrms.app.mapper.OrganizationMapper;
import com.hrms.app.repository.OrganizationRepository;
import com.hrms.app.repository.UserRoleRepository;
import com.hrms.app.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public OrganizationResponseDto addOrganization(OrganizationRequestDto organizationRequestDto) {

        Organization organization = OrganizationMapper.organizationRequestDtoToOrganization(organizationRequestDto);

        organization.setPassword(passwordEncoder.encode(organizationRequestDto.getPassword()));

        Optional<UserRole> role = this.userRoleRepository.findById(502);

        if (role.isEmpty())
            throw new RuntimeException("Role not found");

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(role.get());

        organization.setRoles(userRoles);

        Organization savedOrganization = organizationRepository.save(organization);

        return OrganizationMapper.organizationToOrganizationResponseDto(savedOrganization);
    }

    @Override
    public OrganizationResponseDto getOrganization(UUID organizationCode) {

        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);

        if (organization == null)
            throw new RuntimeException("Invalid Organization Code");

        return OrganizationMapper.organizationToOrganizationResponseDto(organization);
    }

    @Override
    public List<OrganizationResponseDto> getAllOrganization() {

        List<Organization> organizationList = organizationRepository.findAll();

        if (organizationList.isEmpty())
            throw new RuntimeException("No organization found please add a organization first");

        List<OrganizationResponseDto> organizationResponseDtoList = new ArrayList<>();

        for (Organization organization : organizationList) {
            organizationResponseDtoList.add(OrganizationMapper.organizationToOrganizationResponseDto(organization));
        }

        return organizationResponseDtoList;
    }
}
