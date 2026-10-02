package com.ridelink.driver.controller;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.entity.AvailabilityStatus;
import com.ridelink.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Driver & Vehicle Management", description = "Driver profiles, vehicle registration, availability, location tracking and eligible driver discovery")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "driver-service"));
    }

    @PostMapping
    @Operation(summary = "Create driver operational profile with vehicle details")
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody CreateDriverRequest req) {
        DriverResponse response = driverService.createDriver(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get driver profile by driver ID")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        DriverResponse response = driverService.getDriverById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get driver profile by account ID")
    public ResponseEntity<DriverResponse> getDriverByAccountId(@PathVariable Long accountId) {
        DriverResponse response = driverService.getDriverByAccountId(accountId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update driver availability status (AVAILABLE, UNAVAILABLE, ON_RIDE)")
    public ResponseEntity<DriverResponse> updateAvailability(
            @PathVariable Long id,
            @RequestParam AvailabilityStatus status) {
        DriverResponse response = driverService.updateAvailability(id, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/location")
    @Operation(summary = "Update driver GPS coordinates and service area")
    public ResponseEntity<DriverResponse> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLocationRequest req) {
        DriverResponse response = driverService.updateLocation(id, req);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/eligible")
    @Operation(summary = "Find eligible available drivers filtered by service area (Interservice API)")
    public ResponseEntity<List<DriverResponse>> getEligibleDrivers(
            @RequestParam(required = false) String serviceArea) {
        List<DriverResponse> responses = driverService.getEligibleDrivers(serviceArea);
        return ResponseEntity.ok(responses);
    }
}
