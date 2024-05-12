package com.hrms.app.leaveDto;


import com.hrms.app.entity.LeaveRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveResponse {

    private int statusCode;
    private String message;
    private List<LeaveRequest> leaveRequestList;
}
