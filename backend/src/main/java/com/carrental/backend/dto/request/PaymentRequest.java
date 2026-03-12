package com.carrental.backend.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    private Long BookingId;

    private String paymentMethod;

}
