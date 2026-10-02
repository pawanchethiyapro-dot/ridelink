package com.ridelink.fare.dto;

import com.ridelink.fare.entity.Fare;

import java.time.LocalDateTime;

public class FareResponse {
    private Long fareId;
    private Long rideId;
    private Long passengerId;
    private Long driverId;
    private Double distanceKm;
    private Integer durationMinutes;
    private Double baseFare;
    private Double distanceFare;
    private Double timeFare;
    private Double totalFare;
    private LocalDateTime calculatedAt;

    public FareResponse() {}

    public FareResponse(Fare fare) {
        this.fareId = fare.getId();
        this.rideId = fare.getRideId();
        this.passengerId = fare.getPassengerId();
        this.driverId = fare.getDriverId();
        this.distanceKm = fare.getDistanceKm();
        this.durationMinutes = fare.getDurationMinutes();
        this.baseFare = fare.getBaseFare();
        this.distanceFare = fare.getDistanceFare();
        this.timeFare = fare.getTimeFare();
        this.totalFare = fare.getTotalFare();
        this.calculatedAt = fare.getCalculatedAt();
    }

    public Long getFareId() { return fareId; }
    public void setFareId(Long fareId) { this.fareId = fareId; }

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

    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}
