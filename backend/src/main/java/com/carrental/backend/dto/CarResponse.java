package com.carrental.backend.dto;

import com.carrental.backend.entity.enums.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarResponse{

    private Long id;
    private String model;
    private int seats;
    private double pricePerDay;
    private CarStatus status;


}
