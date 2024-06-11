package com.hrms.app.entity;

//import com.hrms.app.Enum.Designation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrms.app.Enum.EmployeeType;
import com.hrms.app.Enum.LeaveStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "employee")
@Builder
public class Employee implements UserDetails {

    //Personal Data
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID empId;
    String username;
    String empName;
    @Column(unique = true)
    String empEmail;
    String empPassword;
    @Column(unique = true)
    String empPhone;
    @Enumerated(EnumType.STRING)
    EmployeeType empType;
    String imgUrl;
    Integer empSalary;
    LocalDate dateOfBirth;
    LocalDate joiningDate;
    Boolean activeEmployee;

    List<Integer> salaryRecord;

    //Employee Designation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeList")
    Designation empDesignation;


    //Organization Related Data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeListOfOrg")
    Organization organization;


    //Policy related Data
    Integer shiftNumber;


    //Leave Related Data
    LocalDate lastFlexiLeaveTaken;
    Double casualLeavesLeft;
    Integer optionalLeavesLeft;
    Integer flexiLeavesLeft;
    Integer nationalLeavesLeft;
    Integer personalLeavesLeft;
    Integer leaveCredited;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<Leave> leaveList;


    //Compensation Related Data
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<CompensationWorkRequest> compensationRequestList;


    //Attendance Related Data
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<Attendance> attendanceList;

    Boolean attendanceMarked;


    //Created & Modified data
    @CreatedDate
    LocalDateTime createdAt;
    String createdBy;

    @LastModifiedDate
    LocalDateTime modifiedAt;
    String modifiedBy;


    //Role-Authority Related Data
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authority=this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
        return authority;
    }

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="user_role_relation",
            joinColumns = @JoinColumn(name = "user(id)", referencedColumnName = "empId"),
            inverseJoinColumns = @JoinColumn(name="role(id)", referencedColumnName = "id"))
    Set<UserRole> roles = new HashSet<>();

    @Override
    public String getPassword() {
        return empPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    @PreUpdate
    private void setEmailAsUsernameAndBCryptPass() {
        this.username = this.empEmail;
//	        this.password = this.passwordEncoder(this.password);// Set username as email
    }

    public UUID getOrganizationCode() {
        return organization.getOrganizationCode();
    }

}
