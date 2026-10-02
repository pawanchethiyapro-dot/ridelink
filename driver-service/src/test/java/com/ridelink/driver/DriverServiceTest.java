package com.ridelink.driver;

import com.ridelink.driver.dto.CreateDriverRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.VehicleDto;
import com.ridelink.driver.entity.AvailabilityStatus;
import com.ridelink.driver.entity.DriverProfile;
import com.ridelink.driver.entity.Vehicle;
import com.ridelink.driver.entity.VehicleType;
import com.ridelink.driver.repository.DriverProfileRepository;
import com.ridelink.driver.repository.VehicleRepository;
import com.ridelink.driver.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverProfileRepository driverRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DriverService driverService;

    private DriverProfile profile;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        profile = new DriverProfile(10L, "Kamal Silva", "0779998888", "DL-123456", "Colombo");
        profile.setId(1L);
        profile.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        vehicle = new Vehicle(1L, "Toyota", "Prius", "CAB-1234", VehicleType.CAR, 4);
        vehicle.setId(1L);
    }

    @Test
    @DisplayName("Should create driver profile with vehicle successfully")
    void createDriverSuccess() {
        CreateDriverRequest req = new CreateDriverRequest();
        req.setAccountId(10L);
        req.setFullName("Kamal Silva");
        req.setPhoneNumber("0779998888");
        req.setLicenseNumber("DL-123456");
        req.setServiceArea("Colombo");
        req.setVehicle(new VehicleDto("Toyota", "Prius", "CAB-1234", VehicleType.CAR, 4));

        when(driverRepository.existsByAccountId(10L)).thenReturn(false);
        when(driverRepository.existsByLicenseNumber("DL-123456")).thenReturn(false);
        when(vehicleRepository.existsByLicensePlate("CAB-1234")).thenReturn(false);
        when(driverRepository.save(any(DriverProfile.class))).thenReturn(profile);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        DriverResponse resp = driverService.createDriver(req);

        assertNotNull(resp);
        assertEquals("Kamal Silva", resp.getFullName());
        assertEquals("Colombo", resp.getServiceArea());
        assertEquals(AvailabilityStatus.AVAILABLE, resp.getAvailabilityStatus());
    }

    @Test
    @DisplayName("Should update driver availability status")
    void updateAvailabilitySuccess() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(driverRepository.save(any(DriverProfile.class))).thenReturn(profile);
        when(vehicleRepository.findByDriverId(1L)).thenReturn(Optional.of(vehicle));

        DriverResponse resp = driverService.updateAvailability(1L, AvailabilityStatus.ON_RIDE);

        assertNotNull(resp);
        assertEquals(AvailabilityStatus.ON_RIDE, profile.getAvailabilityStatus());
    }

    @Test
    @DisplayName("Should retrieve eligible available drivers in service area")
    void getEligibleDriversInArea() {
        when(driverRepository.findByServiceAreaIgnoreCaseAndAvailabilityStatus("Colombo", AvailabilityStatus.AVAILABLE))
                .thenReturn(List.of(profile));
        when(vehicleRepository.findByDriverId(1L)).thenReturn(Optional.of(vehicle));

        List<DriverResponse> eligible = driverService.getEligibleDrivers("Colombo");

        assertEquals(1, eligible.size());
        assertEquals("Kamal Silva", eligible.get(0).getFullName());
    }
}
