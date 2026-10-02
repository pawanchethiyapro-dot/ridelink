package com.ridelink.driver.service;

import com.ridelink.driver.dto.*;
import com.ridelink.driver.entity.AvailabilityStatus;
import com.ridelink.driver.entity.DriverProfile;
import com.ridelink.driver.entity.Vehicle;
import com.ridelink.driver.exception.BadRequestException;
import com.ridelink.driver.exception.ResourceNotFoundException;
import com.ridelink.driver.repository.DriverProfileRepository;
import com.ridelink.driver.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverProfileRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public DriverService(DriverProfileRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public DriverResponse createDriver(CreateDriverRequest req) {
        if (driverRepository.existsByAccountId(req.getAccountId())) {
            throw new BadRequestException("Driver profile already exists for account ID: " + req.getAccountId());
        }
        if (driverRepository.existsByLicenseNumber(req.getLicenseNumber())) {
            throw new BadRequestException("License number already registered: " + req.getLicenseNumber());
        }
        if (vehicleRepository.existsByLicensePlate(req.getVehicle().getLicensePlate())) {
            throw new BadRequestException("Vehicle license plate already registered: " + req.getVehicle().getLicensePlate());
        }

        DriverProfile profile = new DriverProfile(
                req.getAccountId(),
                req.getFullName().trim(),
                req.getPhoneNumber().trim(),
                req.getLicenseNumber().trim(),
                req.getServiceArea().trim()
        );
        profile.setCurrentLatitude(req.getInitialLatitude());
        profile.setCurrentLongitude(req.getInitialLongitude());
        profile.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        DriverProfile savedProfile = driverRepository.save(profile);

        Vehicle vehicle = new Vehicle(
                savedProfile.getId(),
                req.getVehicle().getMake().trim(),
                req.getVehicle().getModel().trim(),
                req.getVehicle().getLicensePlate().trim().toUpperCase(),
                req.getVehicle().getVehicleType(),
                req.getVehicle().getCapacity()
        );
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return new DriverResponse(savedProfile, savedVehicle);
    }

    public DriverResponse getDriverById(Long id) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found with ID: " + id));
        Vehicle vehicle = vehicleRepository.findByDriverId(profile.getId()).orElse(null);
        return new DriverResponse(profile, vehicle);
    }

    public DriverResponse getDriverByAccountId(Long accountId) {
        DriverProfile profile = driverRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found for account ID: " + accountId));
        Vehicle vehicle = vehicleRepository.findByDriverId(profile.getId()).orElse(null);
        return new DriverResponse(profile, vehicle);
    }

    @Transactional
    public DriverResponse updateAvailability(Long id, AvailabilityStatus status) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found with ID: " + id));

        profile.setAvailabilityStatus(status);
        DriverProfile updated = driverRepository.save(profile);
        Vehicle vehicle = vehicleRepository.findByDriverId(updated.getId()).orElse(null);

        return new DriverResponse(updated, vehicle);
    }

    @Transactional
    public DriverResponse updateLocation(Long id, UpdateLocationRequest req) {
        DriverProfile profile = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found with ID: " + id));

        profile.setCurrentLatitude(req.getLatitude());
        profile.setCurrentLongitude(req.getLongitude());
        if (req.getServiceArea() != null && !req.getServiceArea().isBlank()) {
            profile.setServiceArea(req.getServiceArea().trim());
        }

        DriverProfile updated = driverRepository.save(profile);
        Vehicle vehicle = vehicleRepository.findByDriverId(updated.getId()).orElse(null);

        return new DriverResponse(updated, vehicle);
    }

    public List<DriverResponse> getEligibleDrivers(String serviceArea) {
        List<DriverProfile> profiles;
        if (serviceArea != null && !serviceArea.isBlank()) {
            profiles = driverRepository.findByServiceAreaIgnoreCaseAndAvailabilityStatus(serviceArea.trim(), AvailabilityStatus.AVAILABLE);
        } else {
            profiles = driverRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        }

        return profiles.stream()
                .map(p -> {
                    Vehicle v = vehicleRepository.findByDriverId(p.getId()).orElse(null);
                    return new DriverResponse(p, v);
                })
                .collect(Collectors.toList());
    }
}
