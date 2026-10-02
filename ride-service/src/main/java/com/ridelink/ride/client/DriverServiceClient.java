package com.ridelink.ride.client;

import com.ridelink.ride.dto.EligibleDriverDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
public class DriverServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DriverServiceClient.class);
    private final RestClient restClient;

    public DriverServiceClient(@Value("${app.services.driver-service-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public List<EligibleDriverDto> getEligibleDrivers(String serviceArea) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/drivers/eligible")
                            .queryParam("serviceArea", serviceArea)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<EligibleDriverDto>>() {});
        } catch (Exception e) {
            log.error("Failed to query Driver Service for eligible drivers: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void updateDriverAvailability(Long driverId, String status) {
        try {
            restClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/drivers/{id}/availability")
                            .queryParam("status", status)
                            .build(driverId))
                    .retrieve()
                    .toBodilessEntity();
            log.info("Successfully updated driver {} availability to {}", driverId, status);
        } catch (Exception e) {
            log.error("Failed to update availability for driver {}: {}", driverId, e.getMessage());
        }
    }
}
