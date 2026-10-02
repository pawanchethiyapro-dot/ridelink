package com.ridelink.ride.dto;

public class FareCalculationResponse {
    private Long fareId;
    private Long rideId;
    private Double baseFare;
    private Double distanceFare;
    private Double timeFare;
    private Double totalFare;

    public FareCalculationResponse() {}

    public Long getFareId() { return fareId; }
    public void setFareId(Long fareId) { this.fareId = fareId; }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public Double getBaseFare() { return baseFare; }
    public void setBaseFare(Double baseFare) { this.baseFare = baseFare; }

    public Double getDistanceFare() { return distanceFare; }
    public void setDistanceFare(Double distanceFare) { this.distanceFare = distanceFare; }

    public Double getTimeFare() { return timeFare; }
    public void setTimeFare(Double timeFare) { this.timeFare = timeFare; }

    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }
}
