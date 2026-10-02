package com.ridelink.ride;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.*;
import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;
import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverServiceClient driverServiceClient;

    @Mock
    private FareServiceClient fareServiceClient;

    @InjectMocks
    private RideService rideService;

    private Ride testRide;

    @BeforeEach
    void setUp() {
        testRide = new Ride(1L, "Colombo Fort", "Bambalapitiya", "Colombo", 5.0);
        testRide.setId(100L);
    }

    @Test
    @DisplayName("Should create ride and assign driver if available")
    void createRideWithDriverAssignment() {
        CreateRideRequest req = new CreateRideRequest(1L, "Colombo Fort", "Bambalapitiya", "Colombo", 5.0);
        EligibleDriverDto driver = new EligibleDriverDto();
        driver.setId(5L);
        driver.setFullName("Sunil");

        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);
        when(driverServiceClient.getEligibleDrivers("Colombo")).thenReturn(List.of(driver));

        RideResponse resp = rideService.createRide(req);

        assertNotNull(resp);
        assertEquals(RideStatus.ASSIGNED, testRide.getStatus());
        assertEquals(5L, testRide.getDriverId());
        verify(driverServiceClient, times(1)).updateDriverAvailability(5L, "ON_RIDE");
    }

    @Test
    @DisplayName("Should create ride as REQUESTED when no eligible driver is available (Negative Scenario 1)")
    void createRideNoDriverAvailable() {
        CreateRideRequest req = new CreateRideRequest(1L, "Galle Fort", "Unawatuna", "Galle", 6.0);
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);
        when(driverServiceClient.getEligibleDrivers("Galle")).thenReturn(Collections.emptyList());

        RideResponse resp = rideService.createRide(req);

        assertNotNull(resp);
        assertEquals(RideStatus.REQUESTED, testRide.getStatus());
        assertNull(testRide.getDriverId());
    }

    @Test
    @DisplayName("Should transition from ASSIGNED to ACCEPTED")
    void acceptRideSuccess() {
        testRide.setStatus(RideStatus.ASSIGNED);
        testRide.setDriverId(5L);

        when(rideRepository.findById(100L)).thenReturn(Optional.of(testRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse resp = rideService.acceptRide(100L, 5L);

        assertNotNull(resp);
        assertEquals(RideStatus.ACCEPTED, testRide.getStatus());
    }

    @Test
    @DisplayName("Should throw InvalidStateTransitionException on invalid state (Negative Scenario 2)")
    void invalidTransitionThrowsException() {
        testRide.setStatus(RideStatus.REQUESTED); // Not yet ASSIGNED!
        when(rideRepository.findById(100L)).thenReturn(Optional.of(testRide));

        assertThrows(InvalidStateTransitionException.class, () -> rideService.acceptRide(100L, 5L));
    }

    @Test
    @DisplayName("Should complete ride, calculate final fare and release driver")
    void completeRideSuccess() {
        testRide.setStatus(RideStatus.IN_PROGRESS);
        testRide.setDriverId(5L);

        FareCalculationResponse fareResp = new FareCalculationResponse();
        fareResp.setTotalFare(662.50);

        when(rideRepository.findById(100L)).thenReturn(Optional.of(testRide));
        when(fareServiceClient.calculateFinalFare(eq(100L), eq(1L), eq(5L), eq(5.0), anyInt())).thenReturn(fareResp);
        when(rideRepository.save(any(Ride.class))).thenReturn(testRide);

        RideResponse resp = rideService.completeRide(100L, 5L);

        assertNotNull(resp);
        assertEquals(RideStatus.COMPLETED, testRide.getStatus());
        assertEquals(662.50, testRide.getFinalFare());
        verify(driverServiceClient, times(1)).updateDriverAvailability(5L, "AVAILABLE");
    }
}
