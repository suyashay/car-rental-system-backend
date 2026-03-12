package com.carrental.backend.dto.response;

import com.carrental.backend.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private double amount;
    private String paymentMethod;
    private PaymentStatus status;
}
