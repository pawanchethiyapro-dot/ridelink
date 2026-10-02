package com.ridelink.fare.dto;

import com.ridelink.fare.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class ProcessPaymentRequest {
    @NotNull(message = "Ride ID is required")
    private Long rideId;

    @NotNull(message = "Payment method is required (CASH, CARD, WALLET)")
    private PaymentMethod paymentMethod;

    private boolean simulateFailure = false;
    private String failureReason;

    public ProcessPaymentRequest() {}

    public ProcessPaymentRequest(Long rideId, PaymentMethod paymentMethod, boolean simulateFailure, String failureReason) {
        this.rideId = rideId;
        this.paymentMethod = paymentMethod;
        this.simulateFailure = simulateFailure;
        this.failureReason = failureReason;
    }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public boolean isSimulateFailure() { return simulateFailure; }
    public void setSimulateFailure(boolean simulateFailure) { this.simulateFailure = simulateFailure; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
}
