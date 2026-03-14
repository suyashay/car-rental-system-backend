package com.carrental.backend.controller;

import com.carrental.backend.dto.response.AuthResponse;
import com.carrental.backend.dto.request.BookingRequest;
import com.carrental.backend.dto.response.BookingResponse;
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

//    @PutMapping("/update-completed")
//    public ResponseEntity<String> updateCompleted(){
//
//        bookingService.updateCompletedBookings();
//        return ResponseEntity.ok("Completed bookings update");
//    }

    @GetMapping("/my")
    public ResponseEntity<?>  getMyBookings(){
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getOwnerBookings() {
        return ResponseEntity.ok(bookingService.getOwnerBookings());
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<?> getBookingsForCar(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingsForCar(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBookings(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(bookingService.getAllBookings(page, size));
    }

}
