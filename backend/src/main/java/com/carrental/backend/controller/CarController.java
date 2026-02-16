package com.carrental.backend.controller;


import com.carrental.backend.dto.AddCarRequest;
import com.carrental.backend.dto.AuthResponse;
import com.carrental.backend.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
