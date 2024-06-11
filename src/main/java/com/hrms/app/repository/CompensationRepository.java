package com.hrms.app.repository;

import com.hrms.app.Enum.CompensationWorkStatus;
import com.hrms.app.entity.CompensationWorkRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CompensationRepository extends JpaRepository<CompensationWorkRequest, UUID> {

    CompensationWorkRequest findByCompensationRequestId(UUID compensationRequestId);

    @Query("SELECT c FROM CompensationWorkRequest c WHERE c.employee.empEmail = :empEmail AND c.requestedForDate = :date")
    CompensationWorkRequest findByEmpEmailAndRequestedForDate(String empEmail, LocalDate date);

    List<CompensationWorkRequest> findByRequestedForDate(LocalDate date);

    @Query("SELECT c FROM CompensationWorkRequest c WHERE c.employee.empEmail = :empEmail AND c.requestedForDate >= :fromDate AND c.requestedForDate <= :toDate AND c.compensationWorkStatus = :status")
    List<CompensationWorkRequest> findRequestedBetweenDateAndStatus(String empEmail, LocalDate fromDate, LocalDate toDate, CompensationWorkStatus status);
}
