package com.hrms.app.entity;

import com.hrms.app.Enum.CompensationWorkStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "compensation_request")
@Builder
public class CompensationWorkRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    UUID compensationRequestId;

    @Enumerated(EnumType.STRING)
    CompensationWorkStatus compensationWorkStatus;

    LocalDate requestedForDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compensationRequestList")
    Employee employee;

    @CreationTimestamp
    LocalDate appliedOnDate;

    @CreatedDate
    LocalDateTime createdAt;
    String createdBy;

    @LastModifiedDate
    LocalDateTime modifiedAt;
    String modifiedBy;
}
