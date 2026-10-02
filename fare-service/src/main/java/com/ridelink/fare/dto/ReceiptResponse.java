package com.ridelink.fare.dto;

import com.ridelink.fare.entity.Fare;
import com.ridelink.fare.entity.Payment;
import com.ridelink.fare.entity.PaymentStatus;

import java.time.LocalDateTime;

public class ReceiptResponse {
    private String receiptNumber;
    private Long rideId;
    private Long passengerId;
    private Long driverId;
    private Double distanceKm;
    private Integer durationMinutes;
    private Double baseFare;
    private Double distanceFare;
    private Double timeFare;
    private Double totalFare;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionReference;
    private LocalDateTime issuedAt;

    public ReceiptResponse() {}

    public ReceiptResponse(Fare fare, Payment payment) {
        this.receiptNumber = "RCP-" + fare.getRideId() + "-" + System.currentTimeMillis() % 100000;
        this.rideId = fare.getRideId();
        this.passengerId = fare.getPassengerId();
        this.driverId = fare.getDriverId();
        this.distanceKm = fare.getDistanceKm();
        this.durationMinutes = fare.getDurationMinutes();
        this.baseFare = fare.getBaseFare();
        this.distanceFare = fare.getDistanceFare();
        this.timeFare = fare.getTimeFare();
        this.totalFare = fare.getTotalFare();
        this.issuedAt = LocalDateTime.now();

        if (payment != null) {
            this.paymentMethod = payment.getPaymentMethod().name();
            this.paymentStatus = payment.getPaymentStatus();
            this.transactionReference = payment.getTransactionReference();
        } else {
            this.paymentMethod = "UNPAID";
            this.paymentStatus = PaymentStatus.PENDING;
            this.transactionReference = "N/A";
        }
    }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getBaseFare() { return baseFare; }
    public void setBaseFare(Double baseFare) { this.baseFare = baseFare; }

    public Double getDistanceFare() { return distanceFare; }
    public void setDistanceFare(Double distanceFare) { this.distanceFare = distanceFare; }

    public Double getTimeFare() { return timeFare; }
    public void setTimeFare(Double timeFare) { this.timeFare = timeFare; }

    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }

    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
}
