//package com.hrms.app.entity;
//
//import com.google.cloud.Role;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//@Entity
//@Data
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Table(name = "employee")
//public class User implements UserDetails {
//
//    static final long serialVersionUID = 1L;
//
//    @Id
//
//    String username;
//
//    String password;
//
//    @Column(name = "account_non_locked")
//    private boolean accountNonLocked;
//
//    @OneToOne
//    @JoinColumn
//    Employee employee;
//
//    List<Role> roles;
//
//    public User() {
//    }
//    public User(String username, String password, boolean accountNonLocked) {
//        this.username = username;
//        this.password = password;
//        this.accountNonLocked = accountNonLocked;
//    }
//
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(() -> "read");
//    }
//
//    public  List<Role> getRoles() {
//        return roles;
//    }
//    @Override
//    public String getPassword() {
//        return password;
//    }
//    public void setPassword(String password) {
//        this.password = password;
//    }
//    @Override
//    public String getUsername() {
//        return username;
//    }
//    public void setUsername(String username) {
//        this.username = username;
//    }
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//    @Override
//    public boolean isAccountNonLocked() {
//        return accountNonLocked;
//    }
//    @Override public boolean isCredentialsNonExpired() {
//        return true;
//    }
//    @Override public boolean isEnabled() {
//        return true;
//    }
//
//    public void setAccountNonLocked(Boolean accountNonLocked) {
//        this.accountNonLocked = accountNonLocked;
//    }
//    public boolean getAccountNonLocked() {
//        return accountNonLocked;
//    }
//
//}
