package com.flight_radar.demo.controller;

import com.flight_radar.demo.dto.CreateFlightRequest;
import com.flight_radar.demo.dto.FlightPositionDTO;
import com.flight_radar.demo.model.FlightPosition;
import com.flight_radar.demo.model.FlightRecord;
import com.flight_radar.demo.service.FlightPositionService;
import com.flight_radar.demo.service.FlightRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightDataController {

    private final FlightRecordService flightRecordService;
    private final FlightPositionService flightPositionService;

    public FlightDataController(FlightRecordService flightRecordService,
                                FlightPositionService flightPositionService) {
        this.flightRecordService = flightRecordService;
        this.flightPositionService = flightPositionService;
    }

    // ==========================================
    // GET ENDPOINTS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<FlightRecord>> getAllFlights() {
        return ResponseEntity.ok(flightRecordService.getAllFlightRecords());
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<FlightRecord> getFlightById(@PathVariable Integer flightId) {
        FlightRecord flightRecord = flightRecordService.getFlightRecordById(flightId);
        return ResponseEntity.ok(flightRecord);
    }

    @GetMapping("/{flightId}/positions")
    public ResponseEntity<List<FlightPosition>> getPositions(@PathVariable Integer flightId) {
        return ResponseEntity.ok(flightPositionService.findAllByFlightId(flightId));
    }

    @GetMapping("/positions")
    public ResponseEntity<List<FlightPosition>> getPositions(@RequestParam(required=false) LocalDateTime dateTime) {
        if (dateTime == null) {
            return ResponseEntity.ok(flightPositionService.getAllFlightPositions());
        }
        return ResponseEntity.ok(flightPositionService.findAllByDateTime(dateTime));
    }

    // ==========================================
    // POST ENDPOINTS
    // ==========================================

    @PostMapping
    public ResponseEntity<FlightRecord> createFlight(@Valid @RequestBody CreateFlightRequest req) {
        FlightRecord savedRecord = flightRecordService.createNewFlightRecord(
                req.startDate(),
                req.startLatitude(),
                req.startLongitude(),
                req.startLocationName(),
                req.endLatitude(),
                req.endLongitude(),
                req.startDate()
        );
        return ResponseEntity.ok(savedRecord);
    }

    @PostMapping("/{flightId}/positions")
    public ResponseEntity<FlightPosition> addPosition(@PathVariable Integer flightId,
                                                      @Valid @RequestBody FlightPositionDTO flightPositionDTO) {
        FlightPosition savedPosition = flightPositionService.createNewPosition(
                flightId,
                flightPositionDTO.latitude(),
                flightPositionDTO.longitude(),
                flightPositionDTO.time()
        );
        return ResponseEntity.ok(savedPosition);
    }
}