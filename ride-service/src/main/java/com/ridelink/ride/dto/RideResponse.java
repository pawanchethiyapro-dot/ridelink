package com.ridelink.ride.dto;

import com.ridelink.ride.entity.Ride;
import com.ridelink.ride.entity.RideStatus;

import java.time.LocalDateTime;

public class RideResponse {
    private Long id;
    private Long passengerId;
    private Long driverId;
    private String pickupLocation;
    private String destinationLocation;
    private String serviceArea;
    private Double estimatedDistanceKm;
    private RideStatus status;
    private Double estimatedFare;
    private Double finalFare;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public RideResponse() {}

    public RideResponse(Ride ride) {
        this.id = ride.getId();
        this.passengerId = ride.getPassengerId();
        this.driverId = ride.getDriverId();
        this.pickupLocation = ride.getPickupLocation();
        this.destinationLocation = ride.getDestinationLocation();
        this.serviceArea = ride.getServiceArea();
        this.estimatedDistanceKm = ride.getEstimatedDistanceKm();
        this.status = ride.getStatus();
        this.estimatedFare = ride.getEstimatedFare();
        this.finalFare = ride.getFinalFare();
        this.cancellationReason = ride.getCancellationReason();
        this.createdAt = ride.getCreatedAt();
        this.completedAt = ride.getCompletedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDestinationLocation() { return destinationLocation; }
    public void setDestinationLocation(String destinationLocation) { this.destinationLocation = destinationLocation; }

    public String getServiceArea() { return serviceArea; }
    public void setServiceArea(String serviceArea) { this.serviceArea = serviceArea; }

    public Double getEstimatedDistanceKm() { return estimatedDistanceKm; }
    public void setEstimatedDistanceKm(Double estimatedDistanceKm) { this.estimatedDistanceKm = estimatedDistanceKm; }

    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    public Double getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(Double estimatedFare) { this.estimatedFare = estimatedFare; }

    public Double getFinalFare() { return finalFare; }
    public void setFinalFare(Double finalFare) { this.finalFare = finalFare; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
