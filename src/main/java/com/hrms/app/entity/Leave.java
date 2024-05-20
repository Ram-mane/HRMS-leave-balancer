package com.hrms.app.entity;


import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.Enum.LeaveType;
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
@Table(name = "leaves")
@Builder
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID leaveId;

    UUID uniqueLeaveId;

    @Enumerated(EnumType.STRING)
    LeaveType leaveType;

    String leaveReason;

    @Enumerated(EnumType.STRING)
    LeaveStatus leaveStatus;

    int leaveDuration;

    LocalDate leaveStartDate;

    LocalDate leaveEndDate;

    @CreationTimestamp
    LocalDate appliedDate;

    @ManyToOne
    @JoinColumn
    Employee employee;

    @CreatedDate
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime modifiedAt;

    String createdBy;
    String modifiedBy;
}
