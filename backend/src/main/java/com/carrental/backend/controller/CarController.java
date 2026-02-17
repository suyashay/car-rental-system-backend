package com.carrental.backend.controller;


import com.carrental.backend.dto.AddCarRequest;
import com.carrental.backend.dto.AuthResponse;
import com.carrental.backend.dto.CarResponse;
import com.carrental.backend.entity.enums.CarStatus;
import com.carrental.backend.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> addCar(
            @Valid @RequestBody AddCarRequest request,
            @RequestParam Long ownerId) {
        return ResponseEntity.ok(carService.addCar(request,ownerId));
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CarResponse>> searchBySeats(@RequestParam int seats) {
        return ResponseEntity.ok(carService.searchBySeats(seats));
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarResponse>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }
}
