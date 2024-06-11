package com.hrms.app.service;

import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DesignationService {

    String addDesignation(String designation);

    List<String> getAllDesignation();

    PageResponseDto getAllEmpWithDesignation(String designation, int pageNo, UUID organizationCode) throws Exception;
}
