package com.carrental.backend.entity;


import com.carrental.backend.entity.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; //SUCCESS, FAILED, REFUNDED

    private String paymentMethod;

    private String transactionId;

    private LocalDateTime createdAt;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

}
