package com.flight_radar.demo.dto;

import java.time.LocalDateTime;

public record FlightPositionDTO(
        Double lat,
        Double lng,
        LocalDateTime time
) {}