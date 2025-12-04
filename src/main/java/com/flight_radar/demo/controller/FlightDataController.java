package com.flight_radar.demo.controller;

import com.flight_radar.demo.dto.CreateFlightRequest;
import com.flight_radar.demo.dto.FlightDTO;
import com.flight_radar.demo.dto.FlightPositionDTO;
import com.flight_radar.demo.model.FlightPosition;
import com.flight_radar.demo.model.FlightRecord;
import com.flight_radar.demo.service.FlightPositionService;
import com.flight_radar.demo.service.FlightRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:4321")
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
    public ResponseEntity<List<FlightDTO>> getFlights(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime
    ) {
        System.out.println(dateTime);
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        List<FlightDTO> flights = flightRecordService.findAllByDateTime(dateTime);
        return ResponseEntity.ok(flights);
    }


    @GetMapping("/{flightId}")
    public ResponseEntity<FlightRecord> getFlightById(@PathVariable Integer flightId) {
        FlightRecord flightRecord = flightRecordService.getFlightRecordById(flightId);
        return ResponseEntity.ok(flightRecord);
    }

    @GetMapping("/{flightId}/positions")
    public ResponseEntity<List<FlightPositionDTO>> getPositions(
            @PathVariable Integer flightId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @RequestParam(required = false) Integer maxSize
    ) {
        if (maxSize == null) {
            maxSize = Integer.MAX_VALUE;
        }
        if (dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        return ResponseEntity.ok(flightRecordService.getAllFlightPositionsByDate(flightId, dateTime, maxSize));
    }
//
//    @GetMapping("/positions")
//    public ResponseEntity<List<FlightPosition>> getPositions(@RequestParam(required=false) LocalDateTime dateTime) {
//        if (dateTime == null) {
//            return ResponseEntity.ok(flightPositionService.getAllFlightPositions());
//        }
//        return ResponseEntity.ok(flightPositionService.findAllByDateTime(dateTime));
//    }
//
//    // ==========================================
//    // POST ENDPOINTS
//    // ==========================================
//
    @PostMapping
    public ResponseEntity<FlightRecord> createFlight(@Valid @RequestBody CreateFlightRequest req) {
        System.out.println(req.startDate());
        FlightRecord savedRecord = flightRecordService.createNewFlightRecord(
                req.startLatitude(),
                req.startLongitude(),
                req.startLocationName(),
                req.endLatitude(),
                req.endLongitude(),
                req.endLocationName()
        );
        return ResponseEntity.ok(savedRecord);
    }

    @PostMapping("/{flightId}/positions")
    public ResponseEntity<FlightPosition> addPosition(@PathVariable Integer flightId,
                                                      @Valid @RequestBody FlightPositionDTO flightPositionDTO) {
        LocalDateTime utcNow = LocalDateTime.now(ZoneOffset.UTC);


        FlightPosition savedPosition = flightPositionService.createNewPosition(
                flightId,
                flightPositionDTO.lat(),
                flightPositionDTO.lng(),
                flightPositionDTO.time() != null ? flightPositionDTO.time() : utcNow
        );
        return ResponseEntity.ok(savedPosition);
    }

    @PostMapping("/{flightId}/positions/bulk")
    public ResponseEntity<Void> addPositionsBulk(@PathVariable Integer flightId,
                                                 @Valid @RequestBody List<FlightPositionDTO> positions) {
        flightPositionService.addPositionsBulk(flightId, positions);
        return ResponseEntity.ok().build();
    }
}