package com.hrms.app.service;

import com.hrms.app.dto.requestDto.AddPolicyRequestDto;
import com.hrms.app.dto.requestDto.UpdatePolicyRequestDto;
import com.hrms.app.dto.responseDto.PolicyResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface PolicyService {

    PolicyResponseDto addPolicy(AddPolicyRequestDto addPolicyRequestDto);

    PolicyResponseDto getPolicy(UUID organizationCode);

    PolicyResponseDto updatePolicy(UpdatePolicyRequestDto updatePolicyRequestDto);

}
