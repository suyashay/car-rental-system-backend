package com.carrental.backend.scheduler;

import com.carrental.backend.service.BookingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingScheduler {

    private final BookingService bookingService;

    public BookingScheduler(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 600000)
    public void runCompletionJob() {

        bookingService.updateCompletedBookings();

    }
}
