package com.hrms.app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "designation")
@Builder
public class Designation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @Column(unique = true)
    String designation;

    @OneToMany(mappedBy = "empDesignation", cascade = CascadeType.ALL)
    List<Employee> employeeList;

    public Designation(String designation) {
        this.designation = designation;
        employeeList = new ArrayList<>();
    }
}
