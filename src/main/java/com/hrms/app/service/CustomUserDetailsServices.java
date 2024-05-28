package com.hrms.app.service;

import com.hrms.app.entity.Employee;
import com.hrms.app.repository.EmpInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CustomUserDetailsServices implements UserDetailsService {

    @Autowired
    EmpInfoRepository empInfoRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = empInfoRepository.findByEmpEmail(username);
        return employee;
    }
}
