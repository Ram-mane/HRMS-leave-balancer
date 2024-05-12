package com.hrms.app.service.impl;

import com.hrms.app.entity.EmployeeInfo;
import com.hrms.app.entity.LeaveRequest;
import com.hrms.app.leaveDto.LeaveResponse;
import com.hrms.app.repository.EmpInfoRepository;
import com.hrms.app.repository.LeaveRepository;
import com.hrms.app.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmpInfoRepository empInfoRepository;

    @Override
    public LeaveRequest applyLeave(LeaveRequest leaveRequest) {

        if(leaveRequest != null){

            leaveRequest.setLeaveStatus("Pending");
            return leaveRepository.save(leaveRequest);
        }

        return null;
    }

    @Override
    public LeaveResponse getPendingLeaveRequest() {



       List< LeaveRequest> leaveRequest = leaveRepository.getPendingLeaveRequest();

         if(leaveRequest != null){
              return new LeaveResponse(0,"Success",leaveRequest);
            }
        return null;
    }

    @Override
    public LeaveResponse getApprovedLeaveRequest() {
        return null;
    }


    @Override
    public LeaveRequest aproveLeave(LeaveRequest leaveRequest, String empEmail) {



        LeaveRequest appliedLeave = leaveRepository.findByEmpEmail(empEmail);

        if(appliedLeave != null && leaveRequest != null){
            appliedLeave.setLeaveStatus(leaveRequest.getLeaveStatus());
//            appliedLeave.setLeaveDuration(leaveRequest.getLeaveDuration());

            EmployeeInfo empInfo = empInfoRepository.findByEmpEmail(empEmail);
            empInfo.setTotalLeavesLeft(empInfo.getTotalLeavesLeft() - leaveRequest.getLeaveDuration());

            switch(leaveRequest.getLeaveType()){
                case "Casual_Leave":
                    empInfo.setCasualLeavesLeft(empInfo.getCasualLeavesLeft() - leaveRequest.getLeaveDuration());
                    break;
                case "Optional_Leave":
                    empInfo.setOptionalLeavesLeft(empInfo.getOptionalLeavesLeft() - leaveRequest.getLeaveDuration());
                    break;

                case "Flexi_Leave":
                    empInfo.setFlexiLeavesLeft(empInfo.getFlexiLeavesLeft() - leaveRequest.getLeaveDuration());
                    break;
                default:
                    empInfo.setEmergancyLeavesTaken(empInfo.getEmergancyLeavesTaken() + leaveRequest.getLeaveDuration());
                    break;
            }

            empInfoRepository.save(empInfo);

            return leaveRepository.save(appliedLeave);
        }
        return null;
    }
}
