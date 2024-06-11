package com.hrms.app.repository;

import com.hrms.app.entity.Organization;
import com.hrms.app.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PoliciesRepository extends JpaRepository<Policy, UUID> {

    Policy findByPolicyCode(UUID policyCode);
}
