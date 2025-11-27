package com.flight_radar.demo.dto;

import java.time.LocalDateTime;

public record CreateFlightRequest(
        LocalDateTime startDate,
        Double startLatitude,
        Double startLongitude,
        String startLocationName,
        Double endLatitude,
        Double endLongitude,
        String endLocationName
        ) {}