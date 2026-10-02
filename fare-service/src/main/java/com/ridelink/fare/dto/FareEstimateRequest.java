package com.ridelink.fare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FareEstimateRequest {
    @NotNull(message = "Distance in km is required")
    @Min(value = 1, message = "Distance must be at least 1 km")
    private Double distanceKm;

    private String vehicleType = "CAR";

    public FareEstimateRequest() {}
    public FareEstimateRequest(Double distanceKm, String vehicleType) {
        this.distanceKm = distanceKm;
        this.vehicleType = vehicleType;
    }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}
