package com.ridelink.fare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fares")
public class Fare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long rideId;

    @Column(nullable = false)
    private Long passengerId;

    @Column(nullable = false)
    private Long driverId;

    @Column(nullable = false)
    private Double distanceKm;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Double baseFare;

    @Column(nullable = false)
    private Double distanceFare;

    @Column(nullable = false)
    private Double timeFare;

    @Column(nullable = false)
    private Double totalFare;

    @Column(nullable = false, updatable = false)
    private LocalDateTime calculatedAt = LocalDateTime.now();

    public Fare() {}

    public Fare(Long rideId, Long passengerId, Long driverId, Double distanceKm, Integer durationMinutes,
                Double baseFare, Double distanceFare, Double timeFare, Double totalFare) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.baseFare = baseFare;
        this.distanceFare = distanceFare;
        this.timeFare = timeFare;
        this.totalFare = totalFare;
        this.calculatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
