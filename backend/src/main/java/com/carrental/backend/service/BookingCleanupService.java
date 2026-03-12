package com.carrental.backend.service;

import com.carrental.backend.entity.Booking;
import com.carrental.backend.entity.enums.BookingStatus;
import com.carrental.backend.repository.BookingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingCleanupService {

    private final BookingRepository bookingRepository;

    public BookingCleanupService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Scheduled(fixedRate = 60000) // every 60 seconds
    public void cancelExpiredBookings() {

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);

        List<Booking> expiredBookings =
                bookingRepository.findByStatusAndCreatedAtBefore(
                        BookingStatus.PENDING_PAYMENT,
                        threshold
                );

        for (Booking booking : expiredBookings) {

            booking.setStatus(BookingStatus.CANCELLED);

        }

        bookingRepository.saveAll(expiredBookings);

        if(!expiredBookings.isEmpty()){
            System.out.println("Expired bookings cancelled: " + expiredBookings.size());
        }
    }
}