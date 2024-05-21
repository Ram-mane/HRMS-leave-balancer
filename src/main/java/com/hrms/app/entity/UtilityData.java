package com.hrms.app.entity;

import com.hrms.app.Enum.EmployeeType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "utility_data")
public class UtilityData {

    @Id
    int id;

    //table to keep track of no. if casual leaves per employee
    @ElementCollection
    @MapKeyColumn(name="EmpType")
    @Column(name="casualLeaves")
    @CollectionTable(name="EmployeeType", joinColumns=@JoinColumn(name="example_id"))
    Map<EmployeeType, Integer> monthlyLeaveAllocationMap;

    //common table for national holiday
    @ElementCollection
    @MapKeyColumn(name="date1")
    @Column(name="national")
    @CollectionTable(name="NationalHolidays", joinColumns=@JoinColumn(name="example_id1"))
    Map<LocalDate, String> nationalHolidays;

    //common table for optional holiday
    @ElementCollection
    @MapKeyColumn(name="date2")
    @Column(name="optional")
    @CollectionTable(name="OptionalHolidays", joinColumns=@JoinColumn(name="example_id2"))
    Map<LocalDate, String> optionalHolidays;

}
