package com.ridelink.fare.dto;

import com.ridelink.fare.entity.Payment;
import com.ridelink.fare.entity.PaymentMethod;
import com.ridelink.fare.entity.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponse {
    private Long paymentId;
    private Long fareId;
    private Long rideId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionReference;
    private String failureReason;
    private LocalDateTime paidAt;

    public PaymentResponse() {}

    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getId();
        this.fareId = payment.getFareId();
        this.rideId = payment.getRideId();
        this.amount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentStatus = payment.getPaymentStatus();
        this.transactionReference = payment.getTransactionReference();
        this.failureReason = payment.getFailureReason();
        this.paidAt = payment.getPaidAt();
    }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getFareId() { return fareId; }
    public void setFareId(Long fareId) { this.fareId = fareId; }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
