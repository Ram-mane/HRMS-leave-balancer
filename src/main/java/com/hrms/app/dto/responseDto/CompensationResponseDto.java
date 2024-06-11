package com.hrms.app.dto.responseDto;

import com.hrms.app.Enum.CompensationWorkStatus;
import com.hrms.app.entity.Employee;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CompensationResponseDto {

    String empEmail;

    UUID compensationRequestId;

    LocalDate requestedForDate;

    CompensationWorkStatus compensationWorkStatus;

    LocalDate appliedOnDate;
}
