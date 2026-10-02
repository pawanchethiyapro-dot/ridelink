package com.ridelink.driver.dto;

import com.ridelink.driver.entity.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateAvailabilityRequest {
    @NotNull(message = "Availability status is required (AVAILABLE, UNAVAILABLE, ON_RIDE)")
    private AvailabilityStatus status;

    public UpdateAvailabilityRequest() {}
    public UpdateAvailabilityRequest(AvailabilityStatus status) {
        this.status = status;
    }

    public AvailabilityStatus getStatus() { return status; }
    public void setStatus(AvailabilityStatus status) { this.status = status; }
}
