package com.flight_radar.demo.dto;

import java.time.LocalDateTime;

public record CreateFlightRequest(
        String startLocationName,
        Double startLatitude,
        Double startLongitude,
        String endLocationName,
        Double endLatitude,
        Double endLongitude,
        LocalDateTime startDate
) {}