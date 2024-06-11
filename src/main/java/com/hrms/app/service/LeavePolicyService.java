package com.hrms.app.service;

import com.hrms.app.dto.requestDto.LeavePolicyRequestDto;
import com.hrms.app.dto.responseDto.LeavePolicyResponseDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface LeavePolicyService {
    LeavePolicyResponseDto addLeavePolicy(LeavePolicyRequestDto leavePolicyRequestDto);

    LeavePolicyResponseDto updateLeavePolicy(LeavePolicyRequestDto leavePolicyRequestDto);

    LeavePolicyResponseDto getLeavePolicy(UUID organizationCode);
}
