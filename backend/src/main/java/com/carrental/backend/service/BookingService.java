package com.carrental.backend.service;

import com.carrental.backend.dto.response.AuthResponse;
import com.carrental.backend.dto.request.BookingRequest;
import com.carrental.backend.dto.response.BookingResponse;
import com.carrental.backend.entity.Booking;
import com.carrental.backend.entity.Car;
import com.carrental.backend.entity.Driver;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.BookingStatus;
import com.carrental.backend.entity.enums.DriverStatus;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.BookingRepository;
import com.carrental.backend.repository.CarRepository;
import com.carrental.backend.repository.DriverRepository;
import com.carrental.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private static final double DRIVER_CHARGE_PER_DAY = 800;
    private static final double INSURANCE_FEE = 500;
    private static final double FUEL_PACKAGE_FEE = 700;

    private final BookingRepository bookingRepository;

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    private final DriverRepository driverRepository;

    public BookingService(
            BookingRepository bookingRepository,
            CarRepository carRepository,
            UserRepository userRepository,
            DriverRepository driverRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
    }

    private User getAuthenticatedUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public AuthResponse createBooking(BookingRequest request) {

//        User user = userRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = getAuthenticatedUser();

        if(user.getRole() != UserRole.CUSTOMER) {
            throw new RuntimeException("Only Customer are allowed to book cars");
        }

        Car car = carRepository.findCarById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

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
        ) + 1;

        if (days <= 0) {
            throw new RuntimeException("Booking must be at least 1 day");
        }

        Driver assignedDriver = null;

        if(request.isWithDriver()){
            assignedDriver = driverRepository
                    .findFirstByStatus(DriverStatus.AVAILABLE)
                    .orElseThrow(() ->
                            new RuntimeException("No drivers available"));

            assignedDriver.setStatus(DriverStatus.ASSIGNED);
        }

        double totalAmount = calculateTotalPrice(car, request, days);

        Booking booking = new Booking();

        booking.setCar(car);
        booking.setUser(user);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setDriver(assignedDriver);
        booking.setWithDriver(request.isWithDriver());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING_PAYMENT);

        bookingRepository.save(booking);

        return new AuthResponse("confirm the booking by completing the payment");

    }

    private double calculateTotalPrice(Car car, BookingRequest request, long days){

        double total = days * car.getPricePerDay();

        if(request.isWithDriver()){
            total += days * DRIVER_CHARGE_PER_DAY;
        }

        if(request.isInsurance()){
            total += INSURANCE_FEE;
        }

        if(request.isFuelPackage()){
            total += FUEL_PACKAGE_FEE;
        }

        return total;
    }

    public AuthResponse cancelBooking(Long bookingId, Long userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = getAuthenticatedUser();

        if(!booking.getUser().getId().equals(user.getId()) &&  user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }

        if(booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed Bookings can be cancelled");
        }

        if (!booking.getStartDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Cannot cancel booking that has already started or completed");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        if(booking.getDriver() != null) {
            booking.getDriver().setStatus(DriverStatus.AVAILABLE);
        }

        bookingRepository.save(booking);

        return  new AuthResponse("Booking cancelled successfully");
    }

    public List<BookingResponse> getBookingByCustomer(Long userId){
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(b -> new BookingResponse(
                        b.getId(),
                        b.getCar().getId(),
                        b.getCar().getModel(),
                        b.getUser().getEmail(),
                        b.getDriver().getName(),
                        b.getStartDate(),
                        b.getEndDate(),
                        b.getTotalAmount(),
                        b.getStatus()
                ))
                .toList();
    }

    @Transactional
    public void updateCompletedBookings() {

        List<Booking> bookings =
                bookingRepository.findByStatusAndEndDateLessThanEqual(
                        BookingStatus.CONFIRMED,
                        LocalDate.now()
                );

        for (Booking booking : bookings) {

            booking.setStatus(BookingStatus.COMPLETED);

            if (booking.getDriver() != null) {
                booking.getDriver().setStatus(DriverStatus.AVAILABLE);
            }
        }

        bookingRepository.saveAll(bookings);
    }

    private BookingResponse mapToResponse(Booking booking) {

        BookingResponse response = new BookingResponse();

        response.setId(booking.getId());
        response.setCarModel(booking.getCar().getModel());
        response.setCustomerEmail(booking.getUser().getEmail());

        if (booking.getDriver() != null) {
            response.setDriverName(booking.getDriver().getName());
        }

        response.setStartDate(booking.getStartDate());
        response.setEndDate(booking.getEndDate());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus());

        return response;
    }

    public List<BookingResponse> getMyBookings() {
        User user = getAuthenticatedUser();

        List<Booking>  bookings = bookingRepository.findByUserId(user.getId());

        return bookings.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<BookingResponse> getOwnerBookings() {
        User user = getAuthenticatedUser();

        if(user.getRole() != UserRole.OWNER) {
            throw new RuntimeException("only Owner can view bookings");
        }

        List<Booking>  bookings = bookingRepository.findByCarOwnerId(user.getId());

        return bookings.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<BookingResponse> getBookingsForCar(Long carId) {

        User user = getAuthenticatedUser();

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        boolean isAdmin = user.getRole()  == UserRole.ADMIN;
        boolean isOwner = car.getOwner().getId().equals(user.getId());

        if(!isAdmin && !isOwner) {
            throw new RuntimeException("You are not authorized to view this booking");
        }

        List<Booking>  bookings = bookingRepository.findByCarId(carId);

        return bookings.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Page<BookingResponse> getAllBookings(int page, int size) {

        User user = getAuthenticatedUser();

        if(user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only Admin can get all bookings");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Booking> bookings = bookingRepository.findAll(pageable);

        return  bookings.map(this::mapToResponse);
    }

}
