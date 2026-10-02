package com.ridelink.fare;

import com.ridelink.fare.dto.*;
import com.ridelink.fare.entity.Fare;
import com.ridelink.fare.entity.Payment;
import com.ridelink.fare.entity.PaymentMethod;
import com.ridelink.fare.entity.PaymentStatus;
import com.ridelink.fare.repository.FareRepository;
import com.ridelink.fare.repository.PaymentRepository;
import com.ridelink.fare.service.FareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareServiceTest {

    @Mock
    private FareRepository fareRepository;

    @Mock
    private PaymentRepository paymentRepository;

    private FareService fareService;

    @BeforeEach
    void setUp() {
        fareService = new FareService(fareRepository, paymentRepository, 200.0, 80.0, 5.0);
    }

    @Test
    @DisplayName("Should estimate fare accurately based on formula: Base + (Distance * 80)")
    void estimateFareCalculation() {
        FareEstimateRequest req = new FareEstimateRequest(5.0, "CAR");
        FareEstimateResponse resp = fareService.estimateFare(req);

        assertNotNull(resp);
        assertEquals(200.0, resp.getBaseFare());
        assertEquals(400.0, resp.getDistanceFare()); // 5 * 80 = 400
        assertEquals(600.0, resp.getEstimatedTotalFare()); // 200 + 400 = 600
    }

    @Test
    @DisplayName("Should calculate final fare and persist entity")
    void calculateFinalFareSuccess() {
        CalculateFareRequest req = new CalculateFareRequest(100L, 1L, 5L, 10.0, 20);

        Fare savedFare = new Fare(100L, 1L, 5L, 10.0, 20, 200.0, 800.0, 100.0, 1100.0);
        savedFare.setId(1L);

        when(fareRepository.findByRideId(100L)).thenReturn(Optional.empty());
        when(fareRepository.save(any(Fare.class))).thenReturn(savedFare);

        FareResponse resp = fareService.calculateFinalFare(req);

        assertNotNull(resp);
        assertEquals(1100.0, resp.getTotalFare());
        verify(fareRepository, times(1)).save(any(Fare.class));
    }

    @Test
    @DisplayName("Should record simulated successful payment")
    void processPaymentSuccess() {
        Fare fare = new Fare(100L, 1L, 5L, 5.0, 10, 200.0, 400.0, 50.0, 650.0);
        fare.setId(1L);

        Payment payment = new Payment(1L, 100L, 650.0, PaymentMethod.CARD, PaymentStatus.SUCCESS, "TXN-12345", null);
        payment.setId(10L);

        when(fareRepository.findByRideId(100L)).thenReturn(Optional.of(fare));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        ProcessPaymentRequest req = new ProcessPaymentRequest(100L, PaymentMethod.CARD, false, null);
        PaymentResponse resp = fareService.processPayment(req);

        assertNotNull(resp);
        assertEquals(PaymentStatus.SUCCESS, resp.getPaymentStatus());
        assertNotNull(resp.getTransactionReference());
    }

    @Test
    @DisplayName("Should record simulated payment failure (Negative Scenario 3)")
    void processPaymentFailureSimulation() {
        Fare fare = new Fare(100L, 1L, 5L, 5.0, 10, 200.0, 400.0, 50.0, 650.0);
        fare.setId(1L);

        Payment failedPayment = new Payment(1L, 100L, 650.0, PaymentMethod.CARD, PaymentStatus.FAILED, null, "Insufficient funds");
        failedPayment.setId(11L);

        when(fareRepository.findByRideId(100L)).thenReturn(Optional.of(fare));
        when(paymentRepository.save(any(Payment.class))).thenReturn(failedPayment);

        ProcessPaymentRequest req = new ProcessPaymentRequest(100L, PaymentMethod.CARD, true, "Insufficient funds");
        PaymentResponse resp = fareService.processPayment(req);

        assertNotNull(resp);
        assertEquals(PaymentStatus.FAILED, resp.getPaymentStatus());
        assertEquals("Insufficient funds", resp.getFailureReason());
    }
}
