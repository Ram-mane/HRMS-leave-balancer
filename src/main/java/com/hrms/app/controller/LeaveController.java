package com.hrms.app.controller;


import com.hrms.app.entity.LeaveRequest;
import com.hrms.app.leaveDto.LeaveResponse;
import com.hrms.app.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/empInfo")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/applyLeave")
    public ResponseEntity<LeaveRequest> applyLeave(@RequestBody LeaveRequest leaveRequest) {

        try{
            leaveService.applyLeave(leaveRequest);
            return ResponseEntity.ok(leaveRequest);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getPendingLeaveRequest")
    public ResponseEntity<LeaveResponse> getPendingLeaveRequest() {

        try{
            leaveService.getPendingLeaveRequest();
            return ResponseEntity.ok(leaveService.getPendingLeaveRequest());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/approveLeave")
    public ResponseEntity<LeaveRequest> approveLeave(@Param("empEmail") String empEmail,
            @RequestBody LeaveRequest leaveRequest) {

        try{
            leaveService.aproveLeave(leaveRequest, empEmail);
            return ResponseEntity.ok(leaveRequest);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}
