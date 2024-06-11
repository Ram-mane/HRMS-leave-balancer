package com.hrms.app.mapper;

import com.hrms.app.Enum.CompensationWorkStatus;
import com.hrms.app.dto.responseDto.CompensationResponseDto;
import com.hrms.app.entity.CompensationWorkRequest;

import java.time.LocalDate;
import java.util.UUID;

public class CompensationMapper {

    public static CompensationWorkRequest createCompensationRequest(LocalDate date) {

        return CompensationWorkRequest.builder()
                .compensationRequestId(UUID.randomUUID())
                .requestedForDate(date)
                .compensationWorkStatus(CompensationWorkStatus.PENDING)
                .build();
    }

    public static CompensationResponseDto compensationRequestToCompensationResponseDto(CompensationWorkRequest request) {

        return CompensationResponseDto.builder()
                .empEmail(request.getEmployee().getEmpEmail())
                .compensationRequestId(request.getCompensationRequestId())
                .requestedForDate(request.getRequestedForDate())
                .compensationWorkStatus(request.getCompensationWorkStatus())
                .appliedOnDate(request.getAppliedOnDate())
                .build();

    }

}
