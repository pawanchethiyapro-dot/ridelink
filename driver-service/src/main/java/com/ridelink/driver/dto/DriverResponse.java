package com.ridelink.driver.dto;

import com.ridelink.driver.entity.AvailabilityStatus;
import com.ridelink.driver.entity.DriverProfile;
import com.ridelink.driver.entity.Vehicle;

public class DriverResponse {
    private Long id;
    private Long accountId;
    private String fullName;
    private String phoneNumber;
    private String licenseNumber;
    private String serviceArea;
    private AvailabilityStatus availabilityStatus;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double rating;
    private VehicleDto vehicle;

    public DriverResponse() {}

    public DriverResponse(DriverProfile profile, Vehicle vehicle) {
        this.id = profile.getId();
        this.accountId = profile.getAccountId();
        this.fullName = profile.getFullName();
        this.phoneNumber = profile.getPhoneNumber();
        this.licenseNumber = profile.getLicenseNumber();
        this.serviceArea = profile.getServiceArea();
        this.availabilityStatus = profile.getAvailabilityStatus();
        this.currentLatitude = profile.getCurrentLatitude();
        this.currentLongitude = profile.getCurrentLongitude();
        this.rating = profile.getRating();
        if (vehicle != null) {
            this.vehicle = new VehicleDto(vehicle.getMake(), vehicle.getModel(), vehicle.getLicensePlate(), vehicle.getVehicleType(), vehicle.getCapacity());
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getServiceArea() { return serviceArea; }
    public void setServiceArea(String serviceArea) { this.serviceArea = serviceArea; }

    public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    public Double getCurrentLatitude() { return currentLatitude; }
    public void setCurrentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; }

    public Double getCurrentLongitude() { return currentLongitude; }
    public void setCurrentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public VehicleDto getVehicle() { return vehicle; }
    public void setVehicle(VehicleDto vehicle) { this.vehicle = vehicle; }
}
