package com.ridelink.fare.service;

import com.ridelink.fare.dto.*;
import com.ridelink.fare.entity.Fare;
import com.ridelink.fare.entity.Payment;
import com.ridelink.fare.entity.PaymentStatus;
import com.ridelink.fare.exception.ResourceNotFoundException;
import com.ridelink.fare.repository.FareRepository;
import com.ridelink.fare.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FareService {

    private final FareRepository fareRepository;
    private final PaymentRepository paymentRepository;

    private final double baseFare;
    private final double ratePerKm;
    private final double ratePerMinute;

    public FareService(FareRepository fareRepository,
                       PaymentRepository paymentRepository,
                       @Value("${app.fare.base-fare:200.0}") double baseFare,
                       @Value("${app.fare.rate-per-km:80.0}") double ratePerKm,
                       @Value("${app.fare.rate-per-minute:5.0}") double ratePerMinute) {
        this.fareRepository = fareRepository;
        this.paymentRepository = paymentRepository;
        this.baseFare = baseFare;
        this.ratePerKm = ratePerKm;
        this.ratePerMinute = ratePerMinute;
    }

    public FareEstimateResponse estimateFare(FareEstimateRequest req) {
        double distanceFare = Math.round(req.getDistanceKm() * ratePerKm * 100.0) / 100.0;
        double total = Math.round((baseFare + distanceFare) * 100.0) / 100.0;
        String rule = String.format("Base: %.2f LKR + (%.2f km * %.2f LKR/km)", baseFare, req.getDistanceKm(), ratePerKm);

        return new FareEstimateResponse(req.getDistanceKm(), baseFare, distanceFare, total, rule);
    }

    @Transactional
    public FareResponse calculateFinalFare(CalculateFareRequest req) {
        // Return existing calculation if already performed for this ride
        Fare existing = fareRepository.findByRideId(req.getRideId()).orElse(null);
        if (existing != null) {
            return new FareResponse(existing);
        }

        double distanceFare = Math.round(req.getDistanceKm() * ratePerKm * 100.0) / 100.0;
        double timeFare = Math.round(req.getDurationMinutes() * ratePerMinute * 100.0) / 100.0;
        double total = Math.round((baseFare + distanceFare + timeFare) * 100.0) / 100.0;

        Fare fare = new Fare(
                req.getRideId(),
                req.getPassengerId(),
                req.getDriverId(),
                req.getDistanceKm(),
                req.getDurationMinutes(),
                baseFare,
                distanceFare,
                timeFare,
                total
        );

        Fare saved = fareRepository.save(fare);
        return new FareResponse(saved);
    }

    @Transactional
    public PaymentResponse processPayment(ProcessPaymentRequest req) {
        Fare fare = fareRepository.findByRideId(req.getRideId())
                .orElseThrow(() -> new ResourceNotFoundException("No fare calculation found for ride ID: " + req.getRideId()));

        Payment payment;
        if (req.isSimulateFailure()) {
            // Negative Scenario 3: Simulated Payment Failure
            payment = new Payment(
                    fare.getId(),
                    req.getRideId(),
                    fare.getTotalFare(),
                    req.getPaymentMethod(),
                    PaymentStatus.FAILED,
                    null,
                    req.getFailureReason() != null ? req.getFailureReason() : "Card issuer declined transaction (simulated)"
            );
        } else {
            // Happy path: Successful payment
            payment = new Payment(
                    fare.getId(),
                    req.getRideId(),
                    fare.getTotalFare(),
                    req.getPaymentMethod(),
                    PaymentStatus.SUCCESS,
                    "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                    null
            );
        }

        Payment savedPayment = paymentRepository.save(payment);
        return new PaymentResponse(savedPayment);
    }

    public ReceiptResponse getReceipt(Long rideId) {
        Fare fare = fareRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("No fare record found for ride ID: " + rideId));

        Payment payment = paymentRepository.findByRideId(rideId).orElse(null);
        return new ReceiptResponse(fare, payment);
    }

    public FareResponse getFareByRideId(Long rideId) {
        Fare fare = fareRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Fare record not found for ride ID: " + rideId));
        return new FareResponse(fare);
    }
}
