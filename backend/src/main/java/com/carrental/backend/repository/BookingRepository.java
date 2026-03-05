package com.carrental.backend.repository;

import com.carrental.backend.entity.Booking;
import com.carrental.backend.entity.Car;
import com.carrental.backend.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Car car,
            LocalDate endDate,
            LocalDate startDate);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatusAndEndDateLessThanEqual(
            BookingStatus status,
            LocalDate date);

}
