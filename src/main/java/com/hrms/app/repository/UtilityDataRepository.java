package com.hrms.app.repository;

import com.hrms.app.entity.UtilityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilityDataRepository extends JpaRepository<UtilityData, Integer> {
}
