package com.carrental.backend.service;

import com.carrental.backend.dto.AddCarRequest;
import com.carrental.backend.dto.AuthResponse;
import com.carrental.backend.dto.CarResponse;
import com.carrental.backend.entity.Car;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.CarStatus;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.CarRepository;
import com.carrental.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    private final UserRepository userRepository;

    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public AuthResponse addCar(AddCarRequest request, Long ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("owner not found"));

        if(owner.getRole() != UserRole.OWNER) {
            throw new RuntimeException("only owners can add cars");
        }

        Car car = new Car();

        car.setOwner(owner);
        car.setModel(request.getModel());
        car.setSeats(request.getSeats());
        car.setStatus(CarStatus.AVAILABLE);
        car.setPricePerDay(request.getPricePerDay());

        carRepository.save(car);
        System.out.println("Owner role: " + owner.getRole());

        return new AuthResponse("car added successfully");
    }

    public List<CarResponse> getAllCars() {

        return carRepository.findAll()
                .stream()
                .map(car -> new CarResponse(
                        car.getId(),
                        car.getModel(),
                        car.getSeats(),
                        car.getPricePerDay(),
                        car.getStatus()))
                .toList();
    }

    public List<CarResponse> searchBySeats(int seats) {

        return carRepository.findBySeats(seats)
                .stream()
                .map(car -> new CarResponse(
                        car.getId(),
                        car.getModel(),
                        car.getSeats(),
                        car.getPricePerDay(),
                        car.getStatus()))
                .toList();
    }

    public List<CarResponse> getAvailableCars() {
        return carRepository.findByStatus(CarStatus.AVAILABLE)
                .stream()
                .map(car -> new CarResponse(
                        car.getId(),
                        car.getModel(),
                        car.getSeats(),
                        car.getPricePerDay(),
                        car.getStatus()))
                .toList();
    }
}
