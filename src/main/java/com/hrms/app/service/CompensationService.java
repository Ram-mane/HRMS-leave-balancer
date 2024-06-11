package com.hrms.app.service;

import com.hrms.app.Enum.CompensationWorkStatus;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.responseDto.CompensationResponseDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface CompensationService {
    CompensationResponseDto applyForCompensationWorkDay(String empEmail, LocalDate date);

    String approveOrRejectCompensationReq(UUID compensationRequestId, CompensationWorkStatus status, UUID organizationCode);

    List<CompensationResponseDto> getCompensationWork(String empEmail, UUID organizationCode);

    CompensationResponseDto getCompensationRequest(UUID compensationRequestId);
}
