package com.ridelink.ride.service;

import com.ridelink.ride.client.DriverServiceClient;
import com.ridelink.ride.client.FareServiceClient;
import com.ridelink.ride.dto.*;
import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;
import com.ridelink.ride.exception.BadRequestException;
import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.exception.ResourceNotFoundException;
import com.ridelink.ride.repository.RideRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;
    private final FareServiceClient fareServiceClient;

    public RideService(RideRepository rideRepository,
                       DriverServiceClient driverServiceClient,
                       FareServiceClient fareServiceClient) {
        this.rideRepository = rideRepository;
        this.driverServiceClient = driverServiceClient;
        this.fareServiceClient = fareServiceClient;
    }

    @Transactional
    public RideResponse createRide(CreateRideRequest req) {
        Ride ride = new Ride(
                req.getPassengerId(),
                req.getPickupLocation().trim(),
                req.getDestinationLocation().trim(),
                req.getServiceArea().trim(),
                req.getEstimatedDistanceKm()
        );

        Ride savedRide = rideRepository.save(ride);

        // Orchestrate Interservice Interaction 1: Find eligible available driver
        List<EligibleDriverDto> eligibleDrivers = driverServiceClient.getEligibleDrivers(req.getServiceArea().trim());
        if (!eligibleDrivers.isEmpty()) {
            EligibleDriverDto selectedDriver = eligibleDrivers.get(0);
            savedRide.setDriverId(selectedDriver.getId());
            savedRide.setStatus(RideStatus.ASSIGNED);

            // Reserve driver in Driver Service
            driverServiceClient.updateDriverAvailability(selectedDriver.getId(), "ON_RIDE");
            savedRide = rideRepository.save(savedRide);
        }

        return new RideResponse(savedRide);
    }

    public RideResponse getRideById(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + id));
        return new RideResponse(ride);
    }

    public List<RideResponse> getRidesByPassenger(Long passengerId) {
        return rideRepository.findByPassengerIdOrderByCreatedAtDesc(passengerId)
                .stream().map(RideResponse::new).collect(Collectors.toList());
    }

    public List<RideResponse> getRidesByDriver(Long driverId) {
        return rideRepository.findByDriverIdOrderByCreatedAtDesc(driverId)
                .stream().map(RideResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public RideResponse acceptRide(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidStateTransitionException("Cannot accept ride: Current status is " + ride.getStatus() + ". Expected ASSIGNED.");
        }
        if (ride.getDriverId() == null || !ride.getDriverId().equals(driverId)) {
            throw new BadRequestException("Driver ID does not match assigned driver for ride: " + rideId);
        }

        ride.setStatus(RideStatus.ACCEPTED);
        return new RideResponse(rideRepository.save(ride));
    }

    @Transactional
    public RideResponse startRide(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidStateTransitionException("Cannot start ride: Current status is " + ride.getStatus() + ". Expected ACCEPTED.");
        }
        if (ride.getDriverId() == null || !ride.getDriverId().equals(driverId)) {
            throw new BadRequestException("Driver ID does not match assigned driver for ride: " + rideId);
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        return new RideResponse(rideRepository.save(ride));
    }

    @Transactional
    public RideResponse completeRide(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new InvalidStateTransitionException("Cannot complete ride: Current status is " + ride.getStatus() + ". Expected IN_PROGRESS.");
        }
        if (ride.getDriverId() == null || !ride.getDriverId().equals(driverId)) {
            throw new BadRequestException("Driver ID does not match assigned driver for ride: " + rideId);
        }

        // Calculate final fare (Interservice Interaction 2)
        int estimatedMinutes = (int) Math.round(ride.getEstimatedDistanceKm() * 2.5); // 2.5 min per km
        FareCalculationResponse fare = fareServiceClient.calculateFinalFare(
                ride.getId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getEstimatedDistanceKm(),
                estimatedMinutes
        );

        ride.setFinalFare(fare != null ? fare.getTotalFare() : ride.getEstimatedFare());
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());

        // Release driver back to AVAILABLE in Driver Service
        driverServiceClient.updateDriverAvailability(driverId, "AVAILABLE");

        return new RideResponse(rideRepository.save(ride));
    }

    @Transactional
    public RideResponse cancelRide(Long rideId, CancelRideRequest req) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new InvalidStateTransitionException("Cannot cancel a completed ride.");
        }
        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidStateTransitionException("Ride is already cancelled.");
        }

        // Release driver if one was assigned
        if (ride.getDriverId() != null) {
            driverServiceClient.updateDriverAvailability(ride.getDriverId(), "AVAILABLE");
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancellationReason(req != null && req.getReason() != null ? req.getReason() : "Cancelled by user");

        return new RideResponse(rideRepository.save(ride));
    }
}
