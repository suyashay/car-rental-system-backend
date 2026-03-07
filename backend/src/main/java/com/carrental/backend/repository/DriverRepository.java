package com.carrental.backend.repository;

import com.carrental.backend.entity.Driver;
import com.carrental.backend.entity.enums.DriverStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Driver d WHERE d.status = :status")
    Optional<Driver> findFirstByStatus(@Param("status") DriverStatus status);

}
