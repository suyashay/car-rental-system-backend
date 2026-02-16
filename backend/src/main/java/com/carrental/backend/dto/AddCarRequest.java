package com.carrental.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCarRequest {

    @NotBlank
    private String model;

    @Min(1)
    private int seats;

    @Positive
    private double pricePerDay;
}
