package com.ridelink.ride.controller;

import com.ridelink.ride.dto.*;
import com.ridelink.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Ride Management", description = "Ride creation, driver assignment, and lifecycle state management")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "ride-service"));
    }

    @PostMapping("/request")
    @Operation(summary = "Create ride request and orchestrate driver assignment")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest req) {
        RideResponse response = rideService.createRide(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ride details by ID")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "Get ride history for passenger")
    public ResponseEntity<List<RideResponse>> getPassengerRides(@PathVariable Long passengerId) {
        return ResponseEntity.ok(rideService.getRidesByPassenger(passengerId));
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get ride history for driver")
    public ResponseEntity<List<RideResponse>> getDriverRides(@PathVariable Long driverId) {
        return ResponseEntity.ok(rideService.getRidesByDriver(driverId));
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Driver accepts assigned ride (ASSIGNED -> ACCEPTED)")
    public ResponseEntity<RideResponse> acceptRide(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        return ResponseEntity.ok(rideService.acceptRide(id, driverId));
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Driver starts the ride (ACCEPTED -> IN_PROGRESS)")
    public ResponseEntity<RideResponse> startRide(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        return ResponseEntity.ok(rideService.startRide(id, driverId));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Driver completes the ride (IN_PROGRESS -> COMPLETED, calculates final fare)")
    public ResponseEntity<RideResponse> completeRide(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        return ResponseEntity.ok(rideService.completeRide(id, driverId));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel ride (releases assigned driver if applicable)")
    public ResponseEntity<RideResponse> cancelRide(
            @PathVariable Long id,
            @RequestBody(required = false) CancelRideRequest req) {
        return ResponseEntity.ok(rideService.cancelRide(id, req));
    }
}
