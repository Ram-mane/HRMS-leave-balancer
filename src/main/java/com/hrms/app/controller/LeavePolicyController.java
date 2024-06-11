package com.hrms.app.controller;


import com.hrms.app.dto.requestDto.LeavePolicyRequestDto;
import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.LeavePolicyResponseDto;
import com.hrms.app.dto.responseDto.ResponseDtoWrapper;
import com.hrms.app.entity.LeavePolicy;
import com.hrms.app.service.LeavePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/leave_policy")
public class LeavePolicyController {

    @Autowired
    LeavePolicyService leavePolicyService;

    @PostMapping("/addLeavePolicy")
    public ResponseEntity addLeavePolicy(@RequestBody LeavePolicyRequestDto leavePolicyRequestDto) {
        try{
            LeavePolicyResponseDto leavePolicyResponseDto = leavePolicyService.addLeavePolicy(leavePolicyRequestDto);
            ResponseDtoWrapper<LeavePolicyResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", leavePolicyResponseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateLeavePolicy")
    public ResponseEntity updateLeavePolicy(@RequestBody LeavePolicyRequestDto leavePolicyRequestDto) {
        try{
            LeavePolicyResponseDto leavePolicyResponseDto = leavePolicyService.updateLeavePolicy(leavePolicyRequestDto);
            ResponseDtoWrapper<LeavePolicyResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", leavePolicyResponseDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getLeavePolicy")
    public ResponseEntity getLeavePolicy(@RequestParam UUID organizationCode) {
        try{
            LeavePolicyResponseDto leavePolicyResponseDto = leavePolicyService.getLeavePolicy(organizationCode);
            ResponseDtoWrapper<LeavePolicyResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", leavePolicyResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e){
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
