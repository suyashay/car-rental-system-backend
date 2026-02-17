package com.carrental.backend.repository;

import com.carrental.backend.entity.Car;
import com.carrental.backend.entity.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findBySeats(int seats);

    List<Car> findByStatus(CarStatus status);

    List<Car> findBySeatsAndStatus(int seats, CarStatus status);
}
