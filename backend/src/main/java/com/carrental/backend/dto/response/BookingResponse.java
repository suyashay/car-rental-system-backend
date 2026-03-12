package com.carrental.backend.dto.response;

import com.carrental.backend.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;

    private Long carId;

    private String carModel;

    private String customerEmail;

    private String driverName;

    private LocalDate startDate;

    private LocalDate endDate;

    private double totalAmount;

    private BookingStatus status;

}
