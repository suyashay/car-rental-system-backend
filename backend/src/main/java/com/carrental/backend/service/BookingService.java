package com.carrental.backend.service;

import com.carrental.backend.dto.AuthResponse;
import com.carrental.backend.dto.BookingRequest;
import com.carrental.backend.dto.BookingResponse;
import com.carrental.backend.entity.Booking;
import com.carrental.backend.entity.Car;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.BookingStatus;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.BookingRepository;
import com.carrental.backend.repository.CarRepository;
import com.carrental.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CarRepository carRepository,
            UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public AuthResponse createBooking(BookingRequest request) {

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        User user = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if(user.getRole() != UserRole.CUSTOMER) {
            throw new RuntimeException("Only Customer are allowed to book cars");
        }

        if(request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        List<Booking> conflicts =
                bookingRepository.findByCarAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        car,
                        request.getEndDate(),
                        request.getStartDate()
                );

        if(!conflicts.isEmpty()) {
            throw new RuntimeException("Booking already exists - car booked for selected dates");
        }

        long days = ChronoUnit.DAYS.between(
                request.getStartDate(),
                request.getEndDate()
        );

        if (days <= 0) {
            throw new RuntimeException("Booking must be at least 1 day");
        }

        double total = days * car.getPricePerDay();

        Booking booking = new Booking();
        booking.setCar(car);
        booking.setUser(user);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalAmount(total);
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);

        return new AuthResponse("Booking confirmed");

    }

    public AuthResponse cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!booking.getUser().getId().equals(userId) &&  user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        if(booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed Bookings can be cancelled");
        }

        if (!booking.getStartDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Cannot cancel booking that has already started or completed");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        return  new AuthResponse("Booking cancelled successfully");
    }

    public List<BookingResponse> getBookingByCustomer(Long userId){
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getCar().getId(),
                        b.getStartDate(),
                        b.getEndDate(),
                        b.getTotalAmount(),
                        b.getStatus()
                ))
                .toList();
    }

    public void updateCompletedBookings() {

        List<Booking> bookings =
                bookingRepository.findByStatusAndEndDateLessThanEqual(
                        BookingStatus.CONFIRMED,
                        LocalDate.now()
                );

        for (Booking booking : bookings) {
            booking.setStatus(BookingStatus.COMPLETED);
        }

        bookingRepository.saveAll(bookings);
    }

}
