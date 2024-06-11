package com.hrms.app.service.impl;

import com.hrms.app.dto.requestDto.LeavePolicyRequestDto;
import com.hrms.app.dto.responseDto.LeavePolicyResponseDto;
import com.hrms.app.entity.LeavePolicy;
import com.hrms.app.entity.Organization;
import com.hrms.app.mapper.LeavePolicyMapper;
import com.hrms.app.repository.LeavePolicyRepository;
import com.hrms.app.repository.OrganizationRepository;
import com.hrms.app.service.LeavePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LeavePolicyServiceImpl implements LeavePolicyService {

    @Autowired
    LeavePolicyRepository leavePolicyRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public LeavePolicyResponseDto addLeavePolicy(LeavePolicyRequestDto leavePolicyRequestDto) {


        System.out.println("getting org here");
        System.out.println("code: "+ leavePolicyRequestDto.getOrganizationCode());

        Organization organization = organizationRepository.findByOrganizationCode(leavePolicyRequestDto.getOrganizationCode());

        if (organization == null)
            throw new RuntimeException("Invalid Organization code");

        System.out.println("org is there");
        LeavePolicy leavePolicy = LeavePolicyMapper.leavePolicyRequestDtoToLeavePolicy(leavePolicyRequestDto);

        leavePolicy.setOrganization(organization);
        organization.setLeavePolicy(leavePolicy);

        organizationRepository.save(organization);

        return LeavePolicyMapper.leavePolicyToLeavePolicyResponseDto(leavePolicy);
    }

    @Override
    public LeavePolicyResponseDto updateLeavePolicy(LeavePolicyRequestDto leavePolicyRequestDto) {

        Organization organization = organizationRepository.findByOrganizationCode(leavePolicyRequestDto.getOrganizationCode());

        if (organization == null)
            throw new RuntimeException("Invalid Organization code");

        LeavePolicy leavePolicy = LeavePolicyMapper.updateLeavePolicy(organization.getLeavePolicy(), leavePolicyRequestDto);

        return LeavePolicyMapper.leavePolicyToLeavePolicyResponseDto(leavePolicy);
    }


    @Override
    public LeavePolicyResponseDto getLeavePolicy(UUID organizationCode) {

        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);

        if (organization == null)
            throw new RuntimeException("Invalid Organization code");

        if(organization.getPolicy() == null) {
            throw new RuntimeException("Leave policy does not found, Add them first");
        }

        return LeavePolicyMapper.leavePolicyToLeavePolicyResponseDto(organization.getLeavePolicy());
    }

}
