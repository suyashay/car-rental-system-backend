package com.carrental.backend.service;

import com.carrental.backend.dto.request.PaymentRequest;
import com.carrental.backend.dto.response.AuthResponse;
import com.carrental.backend.dto.response.PaymentResponse;
import com.carrental.backend.entity.Booking;
import com.carrental.backend.entity.Payment;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.BookingStatus;
import com.carrental.backend.entity.enums.PaymentStatus;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.BookingRepository;
import com.carrental.backend.repository.PaymentRepository;
import com.carrental.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, PaymentRepository paymentRepository1, BookingRepository bookingRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository1;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthResponse processPayment(PaymentRequest request) {

        User user = getAuthenticatedUser();

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if(!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User not authorized to pay for this booking");
        }

        if(paymentRepository.findByBookingId(booking.getId()).isPresent()){
            throw new RuntimeException("Payment already exists");
        }

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new RuntimeException("Booking is not waiting for payment");
        }

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setCreatedAt(LocalDateTime.now());

        boolean success = Math.random() < 0.9;

        if (success) {

            payment.setStatus(PaymentStatus.SUCCESS);

            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            paymentRepository.save(payment);

            return new AuthResponse("Payment successful. Booking confirmed");

        } else {

            payment.setStatus(PaymentStatus.FAILED);

            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            paymentRepository.save(payment);

            return new AuthResponse("Payment failed. Booking cancelled");
        }

    }

    public PaymentResponse getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus()
        );
    }

    public List<Payment> getMyPayments(){

        User user = getAuthenticatedUser();

        return paymentRepository.findByBookingUserId(user.getId());
    }

    public List<Payment> getAllPayments(){

        User user = getAuthenticatedUser();

        if(user.getRole() != UserRole.ADMIN){
            throw new  RuntimeException("Only admins can view all payments");
        }

        return paymentRepository.findAll();
    }
}
