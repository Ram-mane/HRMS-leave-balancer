package com.hrms.app.repository;

import com.hrms.app.entity.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, UUID> {
}
