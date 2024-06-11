package com.hrms.app.controller;

import com.hrms.app.Security.JwtTokenHelper;
import com.hrms.app.dto.requestDto.JwtAuthRequest;
import com.hrms.app.dto.responseDto.JwtResponseDto;
import com.hrms.app.entity.Employee;
//import com.hrms.app.entity.Organization;
//import com.hrms.app.repository.OrganizationRepository;
import com.hrms.app.entity.Organization;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.OrganizationRepository;
//import com.hrms.app.service.CustomOrganizationDetailsServices;
import com.hrms.app.service.CustomUserDetailsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpController {

    @Autowired
    EmpInfoRepository empInfoRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> createToken(@RequestBody JwtAuthRequest request) {

        this.authenticate(request.getUsername(), request.getPassword());

//        Employee employee = empInfoRepository.findByEmpEmail(request.getUsername());
//        System.out.println("Emp roles---: "+employee.getRoles());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String token = this.jwtTokenHelper.generateToken(userDetails);
//        JwtResponseDto response = new JwtResponseDto();
//        response.setToken(token);

        JwtResponseDto response = new JwtResponseDto("Success", true, token,
                userDetails.getUsername());

        return new ResponseEntity<JwtResponseDto>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
      //  try {
            this.authenticationManager.authenticate(authentication);
//        } catch (BadCredentialsException e) {
////            logger.error("Invalid credentials for user: {}", username);
//            throw new BadCredentialsException("Invalid Username or Password!");
//        }
    }

}
