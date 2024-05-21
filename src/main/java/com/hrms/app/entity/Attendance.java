package com.hrms.app.entity;


import com.hrms.app.Enum.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "attendance")
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Enumerated(EnumType.STRING)
    AttendanceStatus attendanceStatus;

    @ManyToOne
    @JoinColumn
    Employee employee;

    @CreationTimestamp
    LocalDate date;

    @CreationTimestamp
    LocalTime time;

    public Attendance(AttendanceStatus attendanceStatus, Employee employee) {
        this.attendanceStatus = attendanceStatus;
        this.employee = employee;
    }
}
