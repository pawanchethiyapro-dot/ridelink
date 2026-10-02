package com.ridelink.fare.dto;

import jakarta.validation.constraints.NotNull;

public class CalculateFareRequest {
    @NotNull(message = "Ride ID is required")
    private Long rideId;

    @NotNull(message = "Passenger ID is required")
    private Long passengerId;

    @NotNull(message = "Driver ID is required")
    private Long driverId;

    @NotNull(message = "Distance in km is required")
    private Double distanceKm;

    @NotNull(message = "Duration in minutes is required")
    private Integer durationMinutes;

    public CalculateFareRequest() {}

    public CalculateFareRequest(Long rideId, Long passengerId, Long driverId, Double distanceKm, Integer durationMinutes) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
    }

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
}
