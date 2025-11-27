package com.flight_radar.demo.dto;

import java.time.LocalDateTime;

public record FlightPositionDTO(
        Double latitude,
        Double longitude,
        LocalDateTime time
) {}

//public class FlightPositionDTO {
//    private final double lat;
//    private final double lon;
//    private final LocalDateTime dateTime;
//    public FlightPositionDTO(double lat, double lon, LocalDateTime dateTime) {
//        this.lat = lat;
//        this.lon = lon;
//        this.dateTime = dateTime;
//    }
//
//    public double getLat() {
//        return lat;
//    }
//    public double getLon() {
//        return lon;
//    }
//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }
//
//
//}
