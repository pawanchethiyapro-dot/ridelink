package com.ridelink.ride.client;

import com.ridelink.ride.dto.FareCalculationRequest;
import com.ridelink.ride.dto.FareCalculationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FareServiceClient {

    private static final Logger log = LoggerFactory.getLogger(FareServiceClient.class);
    private final RestClient restClient;

    public FareServiceClient(@Value("${app.services.fare-service-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public FareCalculationResponse calculateFinalFare(Long rideId, Long passengerId, Long driverId, Double distanceKm, Integer durationMinutes) {
        try {
            FareCalculationRequest req = new FareCalculationRequest(rideId, passengerId, driverId, distanceKm, durationMinutes);
            return restClient.post()
                    .uri("/api/fares/calculate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(req)
                    .retrieve()
                    .body(FareCalculationResponse.class);
        } catch (Exception e) {
            log.error("Failed to calculate final fare via Fare Service: {}", e.getMessage());
            // Fallback calculation: Base 200 + (distance * 80) + (duration * 5)
            FareCalculationResponse fallback = new FareCalculationResponse();
            fallback.setRideId(rideId);
            double total = 200.0 + (distanceKm * 80.0) + (durationMinutes * 5.0);
            fallback.setTotalFare(total);
            return fallback;
        }
    }
}
