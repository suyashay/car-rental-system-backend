package com.carrental.backend.service;

import com.carrental.backend.dto.AddDriverRequest;
import com.carrental.backend.dto.AuthResponse;
import com.carrental.backend.entity.Driver;
import com.carrental.backend.entity.User;
import com.carrental.backend.entity.enums.DriverStatus;
import com.carrental.backend.entity.enums.UserRole;
import com.carrental.backend.repository.DriverRepository;
import com.carrental.backend.repository.UserRepository;
import org.jspecify.annotations.Nullable;
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
