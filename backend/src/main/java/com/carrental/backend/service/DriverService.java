package com.carrental.backend.service;

import com.carrental.backend.dto.request.AddDriverRequest;
import com.carrental.backend.dto.request.DriverRegistrationRequest;
import com.carrental.backend.dto.response.AuthResponse;
import com.carrental.backend.entity.Driver;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.DriverStatus;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.DriverRepository;
import com.carrental.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    private final UserRepository userRepository;

    public DriverService(DriverRepository driverRepository, UserRepository userRepository) {
        this.driverRepository = driverRepository;
        this.userRepository = userRepository;
    }

    private void validateAdmin(User user) {
        if(user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can perform this action");
        }
    }

    private User getAuthenticatedUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public AuthResponse registerDriver(DriverRegistrationRequest request) {
        Driver driver = new Driver();

        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setCarNumber(request.getCarNumber());

        driver.setStatus(DriverStatus.PENDING_VERIFICATION);

        driverRepository.save(driver);

        return new AuthResponse("Driver Registration Submitted for Verification");
    }

    public AuthResponse verifyDriver(Long driverId){
        User user = getAuthenticatedUser();

        if(user.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only admin can verify driver registration");
        }

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setStatus(DriverStatus.AVAILABLE);

        driverRepository.save(driver);

        return new AuthResponse("Driver Verified Successfully");
    }
    
    public AuthResponse addDriver(AddDriverRequest request) {
        User user = getAuthenticatedUser();
        validateAdmin(user);

        Driver driver = new Driver();

        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setStatus(DriverStatus.AVAILABLE);

        driverRepository.save(driver);

        return new AuthResponse("Driver added successfully");
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public AuthResponse updateDriverStatus(Long driverId, DriverStatus status) {
        User user = getAuthenticatedUser();
        validateAdmin(user);

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setStatus(status);

        driverRepository.save(driver);

        return new AuthResponse("Driver status updated successfully");
    }

}
