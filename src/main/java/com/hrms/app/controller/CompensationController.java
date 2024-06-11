package com.hrms.app.controller;

import com.hrms.app.Enum.CompensationWorkStatus;
import com.hrms.app.Enum.LeaveStatus;
import com.hrms.app.dto.responseDto.CompensationResponseDto;
import com.hrms.app.dto.responseDto.ResponseDtoListWrapper;
import com.hrms.app.dto.responseDto.ResponseDtoWrapper;
import com.hrms.app.service.CompensationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/compensation_request")
public class CompensationController {

    @Autowired
    CompensationService compensationService;

    @PostMapping("/applyForCompensationWork")
    public ResponseEntity applyForCompensationWorkDay(@RequestParam String empEmail,
                                                      @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date) {
        try{
            CompensationResponseDto responseDto = compensationService.applyForCompensationWorkDay(empEmail, date);
            ResponseDtoWrapper<CompensationResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", responseDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/approveCompensationWorkDay")
    public ResponseEntity approveOrRejectCompensationReq(@RequestParam UUID compensationRequestId,
                                                     @RequestParam CompensationWorkStatus status,
                                                     @RequestParam UUID organizationCode) {
        try{
            String message = compensationService.approveOrRejectCompensationReq(compensationRequestId, status, organizationCode);
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(1,
                    "Success", message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCompensationRequest")
    public ResponseEntity getCompensationRequest(@RequestParam UUID compensationRequestId) {
        try{
            CompensationResponseDto compensationResponseDto = compensationService.getCompensationRequest(compensationRequestId);
            ResponseDtoWrapper<CompensationResponseDto> response = new ResponseDtoWrapper<>(1,
                    "Success", compensationResponseDto);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCompensationWorkList")
    public ResponseEntity getCompensationWork(@RequestParam String empEmail, @RequestParam UUID organizationCode) {
        try{
            List<CompensationResponseDto> compensationWorkList  = compensationService.getCompensationWork(empEmail, organizationCode);
            ResponseDtoListWrapper response = new ResponseDtoListWrapper(1,
                    "Success", compensationWorkList);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }
        catch (Exception e) {
            ResponseDtoWrapper<String> response = new ResponseDtoWrapper<>(0, "Failed", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
