package com.hrms.app.service;

import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Organization;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Transactional
@Service
@Primary
public class CustomUserDetailsServices implements UserDetailsService {

    @Autowired
    EmpInfoRepository empInfoRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Organization organization = organizationRepository.findByUsername(username);

        if(organization != null)
            return organization;

        Employee employee = empInfoRepository.findByEmpEmail(username);
        return employee;
    }

}
