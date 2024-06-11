package com.hrms.app.service;

import com.hrms.app.dto.responseDto.EmployeeResponseDto;
import com.hrms.app.dto.responseDto.GetAttendanceResponseDto;
import com.hrms.app.dto.responseDto.LeaveResponseDto;
import com.hrms.app.dto.responseDto.PageResponseDto;
import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Leave;
import com.hrms.app.mapper.AttendanceMapper;
import com.hrms.app.mapper.EmployeeMapper;
import com.hrms.app.mapper.LeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaginationService{

    @Autowired
    FirebaseService firebaseService;

    public PageResponseDto paginationOnEmployeeList(int pageNo, List<Employee> employeeList) throws Exception {

        int pageSizeLeave = firebaseService.getPageSizeEmp();

        int totalPages = (int) Math.ceil((double) employeeList.size() / pageSizeLeave);

        // Check if requested page number exceeds total pages
        if (pageNo > totalPages) {
            // Return an empty list
            return new PageResponseDto(new ArrayList<>(), pageNo, pageSizeLeave, totalPages, Long.valueOf(employeeList.size()), true);
        }

        int start = (pageNo-1)*pageSizeLeave;
        int end = Math.min(start+pageSizeLeave, employeeList.size());
        Pageable pageable = PageRequest.of(pageNo-1, pageSizeLeave);
        Page<Employee> page = new PageImpl<>(employeeList.subList(start, end), pageable, employeeList.size());

        List<EmployeeResponseDto> employeeResponseDtoList = new ArrayList<>();
        for (Employee employee : page) {
            employeeResponseDtoList.add(EmployeeMapper.employeeToEmployeeResponseDto(employee));
        }

        return new PageResponseDto(employeeResponseDtoList, page.getNumber()+1, page.getSize(),
                page.getTotalPages(), page.getTotalElements(), page.isLast());
    }

    public PageResponseDto paginationOnLeaveList(int pageNo, List<Leave> leaveList) throws Exception {

        int pageSizeLeave = firebaseService.getPageSizeLeave();

        int totalPages = (int) Math.ceil((double) leaveList.size() / pageSizeLeave);

        // Check if requested page number exceeds total pages
        if (pageNo > totalPages) {
            // Return an empty list
            return new PageResponseDto(new ArrayList<>(), pageNo, pageSizeLeave, totalPages, Long.valueOf(leaveList.size()), true);
        }

        int start = (pageNo-1)*pageSizeLeave;
        int end = Math.min(start+pageSizeLeave, leaveList.size());
        Pageable pageable = PageRequest.of(pageNo-1, pageSizeLeave);
        Page<Leave> page = new PageImpl<>(leaveList.subList(start, end), pageable, leaveList.size());

        List<LeaveResponseDto> leaveResponseDtoList = new ArrayList<>();
        for (Leave leave : page) {
            leaveResponseDtoList.add(LeaveMapper.leaveToLeaveResponseDto(leave));
        }

        return new PageResponseDto(leaveResponseDtoList, page.getNumber()+1, page.getSize(),
                page.getTotalPages(), page.getTotalElements(), page.isLast());
    }

    public PageResponseDto paginationOnAttendanceList(int pageNo, List<Attendance> attendanceList) throws Exception {

        int pageSize = firebaseService.getPageSizeEmp();

        int totalPages = (int) Math.ceil((double) attendanceList.size() / pageSize);

        // Check if requested page number exceeds total pages
        if (pageNo > totalPages) {
            // Return an empty list
            return new PageResponseDto(new ArrayList<>(), pageNo, pageSize, totalPages, Long.valueOf(attendanceList.size()), true);
        }

        int start = (pageNo-1)*pageSize;
        int end = Math.min(start+pageSize, attendanceList.size());
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Attendance> page = new PageImpl<>(attendanceList.subList(start, end), pageable, attendanceList.size());

        List<GetAttendanceResponseDto> getAttendanceResponseDtoList = new ArrayList<>();
        for (Attendance attendance : page) {
            getAttendanceResponseDtoList.add(AttendanceMapper.AttendanceToGetAttendanceResponseDto(attendance));
        }

        return new PageResponseDto(getAttendanceResponseDtoList, page.getNumber()+1, page.getSize(),
                page.getTotalPages(), page.getTotalElements(), page.isLast());
    }
}
