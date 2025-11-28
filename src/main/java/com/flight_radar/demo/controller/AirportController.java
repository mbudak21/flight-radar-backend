package com.flight_radar.demo.controller;

import com.flight_radar.demo.model.Airport;
import com.flight_radar.demo.model.FlightPosition;
import com.flight_radar.demo.service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@CrossOrigin(origins = "http://localhost:4321")
public class AirportController {
    private final AirportService airportService;
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public ResponseEntity<List<Airport>> getAllFlightPositions() {
        return ResponseEntity.ok(airportService.findAll());
    }
}
