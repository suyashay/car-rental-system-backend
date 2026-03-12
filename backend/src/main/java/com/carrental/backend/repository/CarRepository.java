package com.carrental.backend.repository;

import com.carrental.backend.entity.Car;
import com.carrental.backend.entity.enums.CarStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Car> findCarById(Long id);

    List<Car> findBySeats(int seats);

    List<Car> findByStatus(CarStatus status);

    List<Car> findBySeatsAndStatus(int seats, CarStatus status);
}
