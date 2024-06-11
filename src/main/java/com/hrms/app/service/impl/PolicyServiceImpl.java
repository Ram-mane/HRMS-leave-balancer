package com.hrms.app.service.impl;

import com.hrms.app.dto.requestDto.AddPolicyRequestDto;
import com.hrms.app.dto.requestDto.UpdatePolicyRequestDto;
import com.hrms.app.dto.responseDto.PolicyResponseDto;
import com.hrms.app.entity.Organization;
import com.hrms.app.entity.Policy;
import com.hrms.app.mapper.PolicyMapper;
import com.hrms.app.repository.OrganizationRepository;
import com.hrms.app.repository.PoliciesRepository;
import com.hrms.app.service.PolicyService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    PoliciesRepository policiesRepository;

    @Override
    public PolicyResponseDto addPolicy(AddPolicyRequestDto addPolicyRequestDto) {
        Policy policy = PolicyMapper.policyRequestDtoToPolicy(addPolicyRequestDto);

        Organization organization = organizationRepository.findByOrganizationCode(addPolicyRequestDto.getOrganizationCode());

        if (organization == null)
            throw new RuntimeException("Invalid Organization Code");

        policy.setOrganization(organization);
        organization.setPolicy(policy);

        organizationRepository.save(organization);

        return PolicyMapper.policyToPolicyResponseDto(policy);
    }

    @Override
    public PolicyResponseDto getPolicy(UUID organizationCode) {

        Organization organization = organizationRepository.findByOrganizationCode(organizationCode);

        if (organization == null)
            throw new RuntimeException("Invalid Organization code");

        if (organization.getPolicy() == null)
            throw new RuntimeException("Policy does not found, Add them first");

        return PolicyMapper.policyToPolicyResponseDto(organization.getPolicy());
    }

    @Override
    public PolicyResponseDto updatePolicy(UpdatePolicyRequestDto updatePolicyRequestDto) {
        Policy policy = policiesRepository.findByPolicyCode(updatePolicyRequestDto.getPolicyCode());

        if (policy == null)
            throw new RuntimeException("Invalid Policy Code");

        policy = PolicyMapper.updatePolicy(updatePolicyRequestDto, policy);

        policiesRepository.save(policy);

        return PolicyMapper.policyToPolicyResponseDto(policy);
    }
}
