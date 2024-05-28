package com.hrms.app.service;

import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DesignationService {

    String addDesignation(String designation);

    List<String> getAllDesignation();

    List<EmployeeResponseDto> getAllEmpWithDesignation(String designation);

}
