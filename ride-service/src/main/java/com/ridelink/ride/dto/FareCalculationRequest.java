package com.ridelink.ride.dto;

public class FareCalculationRequest {
    private Long rideId;
    private Long passengerId;
    private Long driverId;
    private Double distanceKm;
    private Integer durationMinutes;

    public FareCalculationRequest() {}

    public FareCalculationRequest(Long rideId, Long passengerId, Long driverId, Double distanceKm, Integer durationMinutes) {
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
