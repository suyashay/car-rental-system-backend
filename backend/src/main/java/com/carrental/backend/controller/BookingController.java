package com.carrental.backend.controller;

import com.carrental.backend.dto.AuthResponse;
import com.carrental.backend.dto.BookingRequest;
import com.carrental.backend.dto.BookingResponse;
import com.carrental.backend.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AuthResponse> cancelBooking(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, userId));
    }

    @GetMapping("/customer/{userId}")
    public ResponseEntity<List<BookingResponse>> getCustomerBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(
                bookingService.getBookingByCustomer(userId)
        );
    }

    @PutMapping("/update-completed")
    public ResponseEntity<String> updateCompleted(){

        bookingService.updateCompletedBookings();
        return ResponseEntity.ok("Completed bookings update");
    }



}
