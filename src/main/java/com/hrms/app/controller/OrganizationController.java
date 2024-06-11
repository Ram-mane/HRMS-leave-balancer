package com.hrms.app.controller;

import com.hrms.app.dto.requestDto.OrganizationRequestDto;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.dto.responseDto.OrganizationResponseDto;
import com.hrms.app.dto.responseDto.ResponseDtoListWrapper;
import com.hrms.app.dto.responseDto.ResponseDtoWrapper;
import com.hrms.app.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/organization")
@RestController
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @PostMapping("/addOrganization")
    public ResponseEntity addOrganization(@RequestBody OrganizationRequestDto organizationRequestDto) {
        try {
            OrganizationResponseDto organizationResponseDto = organizationService.addOrganization(organizationRequestDto);
            ResponseDtoWrapper<OrganizationResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", organizationResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getOrganization")
    public ResponseEntity getOrganization(@RequestParam UUID organizationCode) {
        try {
            OrganizationResponseDto organizationResponseDto = organizationService.getOrganization(organizationCode);
            ResponseDtoWrapper<OrganizationResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", organizationResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllOrganization")
    public ResponseEntity getAllOrganization() {
        try {
            List<OrganizationResponseDto> organizationResponseDtoList = organizationService.getAllOrganization();
            ResponseDtoListWrapper response = new ResponseDtoListWrapper(1,
                    "Success", organizationResponseDtoList);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
