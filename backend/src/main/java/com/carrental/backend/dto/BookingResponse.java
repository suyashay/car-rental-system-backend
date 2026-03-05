package com.carrental.backend.dto;

import com.carrental.backend.entity.enums.BookingStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookingResponse {

    private Long id;
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
    private BookingStatus status;

    public BookingResponse(Long id,
                           Long carId,
                           LocalDate startDate,
                           LocalDate endDate,
                           double totalAmount,
                           BookingStatus status) {
        this.id = id;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }
}
