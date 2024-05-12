package com.hrms.app.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmployeeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID empId;

    private String empName;
    private String empEmail;
    private String empPhone;
    private String empType;
    private String empDesignation;
    private String imgUrl;
    private long empSalary;


    private long totalLeavesLeft;
    private long casualLeavesLeft;
    private long optionalLeavesLeft;
    private long flexiLeavesLeft;
    private long emergancyLeavesTaken;


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private String createdBy;
    private String modifiedBy;

}
