package com.hrms.app.repository;

import com.hrms.app.entity.Employee;
import com.hrms.app.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Organization findByUsername(String username);

    @Query("SELECT o FROM Organization o WHERE o.organizationCode = :organizationCode")
    Organization findByOrganizationCode(UUID organizationCode);

    @Query("SELECT o.employeeList FROM Organization o WHERE o.organizationCode = :organizationCode")
    Page<Employee> findEmployeesByOrganizationCode(@Param("organizationCode") UUID organizationCode, Pageable pageable);

    @Query("SELECT ELEMENT(o.employeeList) FROM Organization o WHERE o.organizationCode = :orgCode AND ELEMENT(o.employeeList).shiftNumber = :shiftNo")
    List<Employee> findEmployeesByOrganizationCodeAndShiftNo(@Param("orgCode") UUID orgCode, @Param("shiftNo") Integer shiftNumber);

}
