package com.carrental.backend.controller;

import com.carrental.backend.dto.request.AddDriverRequest;
import com.carrental.backend.dto.request.DriverRegistrationRequest;
import com.carrental.backend.entity.enums.DriverStatus;
import com.carrental.backend.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;


    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDriver(@RequestBody DriverRegistrationRequest request) {
        return ResponseEntity.ok(driverService.registerDriver(request));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<?> verifyDriver(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.verifyDriver(id));
    }

    @PostMapping
    public ResponseEntity<?> addDriver(@RequestBody AddDriverRequest request) {
        return ResponseEntity.ok(driverService.addDriver(request));
    }

    @GetMapping
    public ResponseEntity<?> getDriver(){
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam DriverStatus status){
        return ResponseEntity.ok(driverService.updateDriverStatus(id, status));
    }

}
