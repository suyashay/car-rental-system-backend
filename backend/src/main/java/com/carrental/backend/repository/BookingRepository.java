package com.carrental.backend.repository;

import com.carrental.backend.entity.Booking;
import com.carrental.backend.entity.Car;
import com.carrental.backend.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Car car,
            LocalDate endDate,
            LocalDate startDate);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByCarId(Long carId);

    List<Booking> findByCarOwnerId(Long carOwnerId);

    List<Booking> findByStatusAndCreatedAtBefore(
            BookingStatus status,
            LocalDateTime time
    );

    List<Booking> findByStatusAndEndDateLessThanEqual(
            BookingStatus status,
            LocalDate today
    );

//    @Modifying
//    @Query("""
//       UPDATE Booking b
//       SET b.status = 'COMPLETED'
//       WHERE b.status = 'CONFIRMED'
//       AND b.endDate < :today
//       """)
//    int updateCompletedBookings(
//            @Param("confirmed") BookingStatus confirmed,
//            @Param("completed")  BookingStatus completed,
//            @Param("today") LocalDate today
//    );
}
