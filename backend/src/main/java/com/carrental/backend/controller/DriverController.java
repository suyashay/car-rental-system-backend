package com.carrental.backend.controller;

import com.carrental.backend.dto.AddDriverRequest;
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
