package com.ridelink.fare.dto;

public class FareEstimateResponse {
    private Double distanceKm;
    private Double baseFare;
    private Double distanceFare;
    private Double estimatedTotalFare;
    private String calculationRule;

    public FareEstimateResponse() {}

    public FareEstimateResponse(Double distanceKm, Double baseFare, Double distanceFare, Double estimatedTotalFare, String calculationRule) {
        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.distanceFare = distanceFare;
        this.estimatedTotalFare = estimatedTotalFare;
        this.calculationRule = calculationRule;
    }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }

    public Double getBaseFare() { return baseFare; }
    public void setBaseFare(Double baseFare) { this.baseFare = baseFare; }

    public Double getDistanceFare() { return distanceFare; }
    public void setDistanceFare(Double distanceFare) { this.distanceFare = distanceFare; }

    public Double getEstimatedTotalFare() { return estimatedTotalFare; }
    public void setEstimatedTotalFare(Double estimatedTotalFare) { this.estimatedTotalFare = estimatedTotalFare; }

    public String getCalculationRule() { return calculationRule; }
    public void setCalculationRule(String calculationRule) { this.calculationRule = calculationRule; }
}
