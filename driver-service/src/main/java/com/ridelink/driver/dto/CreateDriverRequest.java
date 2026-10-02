package com.ridelink.driver.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateDriverRequest {
    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    private Double initialLatitude = 6.9271; // Default to Colombo
    private Double initialLongitude = 79.8612;

    @NotNull(message = "Vehicle details are required")
    @Valid
    private VehicleDto vehicle;

    public CreateDriverRequest() {}

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

    public Double getInitialLatitude() { return initialLatitude; }
    public void setInitialLatitude(Double initialLatitude) { this.initialLatitude = initialLatitude; }

    public Double getInitialLongitude() { return initialLongitude; }
    public void setInitialLongitude(Double initialLongitude) { this.initialLongitude = initialLongitude; }

    public VehicleDto getVehicle() { return vehicle; }
    public void setVehicle(VehicleDto vehicle) { this.vehicle = vehicle; }
}
