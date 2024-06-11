package com.hrms.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "organization")
@Builder
public class Organization implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID orgId;

    @Column(unique = true)
    UUID organizationCode;

    @Column(unique = true)
    String username;

    @Column(unique = true)
    String pocEmail;

    String pocNumber;

    String password;

    String organizationName;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    List<Employee> employeeList;

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL)
    Policy policy;

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL)
    LeavePolicy leavePolicy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authority=this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
        return authority;
    }

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="org_role_relation",
            joinColumns = @JoinColumn(name = "org(id)", referencedColumnName = "orgId"),
            inverseJoinColumns = @JoinColumn(name="role(id)", referencedColumnName = "id"))
    Set<UserRole> roles = new HashSet<>();

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
        this.username = this.pocEmail;
//	        this.password = this.passwordEncoder(this.password);// Set username as email
    }

}
