package com.hrms.app.controller;

import com.hrms.app.dto.requestDto.AddPolicyRequestDto;
import com.hrms.app.dto.requestDto.UpdatePolicyRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.PolicyResponseDto;
import com.hrms.app.dto.responseDto.ResponseDtoWrapper;
import com.hrms.app.repository.OrganizationRepository;
import com.hrms.app.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/policy")
public class PoliciesController {
    @Autowired
    PolicyService policyService;

    @PostMapping("/createPolicy")
    public ResponseEntity addPolicy(@RequestBody AddPolicyRequestDto addPolicyRequestDto) {
        try{
            PolicyResponseDto policyResponseDto = policyService.addPolicy(addPolicyRequestDto);
            ResponseDtoWrapper<PolicyResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", policyResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getPolicy")
    public ResponseEntity getPolicy(@RequestParam UUID organizationCode) {
        try{
            PolicyResponseDto policyResponseDto = policyService.getPolicy(organizationCode);
            ResponseDtoWrapper<PolicyResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", policyResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updatePolicy")
    public ResponseEntity updatePolicy(@RequestBody UpdatePolicyRequestDto updatePolicyRequestDto) {
        try{
            PolicyResponseDto policyResponseDto = policyService.updatePolicy(updatePolicyRequestDto);
            ResponseDtoWrapper<PolicyResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", policyResponseDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
