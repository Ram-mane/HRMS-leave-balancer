package com.hrms.app.repository;

import com.hrms.app.entity.Attendance;
import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {


}
