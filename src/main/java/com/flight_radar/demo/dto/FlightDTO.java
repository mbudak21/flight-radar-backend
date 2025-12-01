package com.flight_radar.demo.dto;

import java.time.LocalDateTime;

// Retrieved at specific Datetime
public record FlightDTO (
        Integer id,
        Double startLat,
        Double startLng,
        String startLocationName,

        Double endLat,
        Double endLng,
        String endLocationName,

        LocalDateTime lastUpdatedAt,
        LocalDateTime departureTime,

        Double posLat,
        Double posLng,

        // Second last point for heading calculations
        Double prevLat,
        Double prevLng
) {}