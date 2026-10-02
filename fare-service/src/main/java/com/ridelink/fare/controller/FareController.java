package com.ridelink.fare.controller;

import com.ridelink.fare.dto.*;
import com.ridelink.fare.service.FareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fares")
@Tag(name = "Fare & Payment Management", description = "Fare estimation, calculation, payment processing and receipt generation")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "fare-service"));
    }

    @PostMapping("/estimate")
    @Operation(summary = "Get fare estimate using documented pricing rule")
    public ResponseEntity<FareEstimateResponse> estimateFare(@Valid @RequestBody FareEstimateRequest req) {
        FareEstimateResponse response = fareService.estimateFare(req);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate")
    @Operation(summary = "Calculate final fare for a completed ride (Interservice API)")
    public ResponseEntity<FareResponse> calculateFinalFare(@Valid @RequestBody CalculateFareRequest req) {
        FareResponse response = fareService.calculateFinalFare(req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/payments")
    @Operation(summary = "Record simulated payment (supports simulating success or negative failure case)")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody ProcessPaymentRequest req) {
        PaymentResponse response = fareService.processPayment(req);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/receipts/{rideId}")
    @Operation(summary = "Retrieve digital receipt for a ride with fare and payment status")
    public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable Long rideId) {
        ReceiptResponse response = fareService.getReceipt(rideId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ride/{rideId}")
    @Operation(summary = "Get fare breakdown by ride ID")
    public ResponseEntity<FareResponse> getFareByRideId(@PathVariable Long rideId) {
        return ResponseEntity.ok(fareService.getFareByRideId(rideId));
    }
}
