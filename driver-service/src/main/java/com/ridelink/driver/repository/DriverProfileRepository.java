package com.ridelink.driver.repository;

import com.ridelink.driver.entity.AvailabilityStatus;
import com.ridelink.driver.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {
    Optional<DriverProfile> findByAccountId(Long accountId);
    boolean existsByAccountId(Long accountId);
    boolean existsByLicenseNumber(String licenseNumber);
    List<DriverProfile> findByServiceAreaIgnoreCaseAndAvailabilityStatus(String serviceArea, AvailabilityStatus availabilityStatus);
    List<DriverProfile> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);
}
