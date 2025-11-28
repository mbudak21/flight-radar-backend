package com.flight_radar.demo.service;

import com.flight_radar.demo.model.FlightPosition;
import com.flight_radar.demo.model.FlightRecord;
import com.flight_radar.demo.repository.FlightPositionRepository;
import com.flight_radar.demo.repository.FlightRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightPositionService {
    private final FlightPositionRepository flightPositionRepository;
    private final FlightRecordRepository flightRecordRepository;
    public FlightPositionService(FlightPositionRepository flightPositionRepository,  FlightRecordRepository flightRecordRepository) {
        this.flightPositionRepository = flightPositionRepository;
        this.flightRecordRepository = flightRecordRepository;
    }

    @Transactional
    public List<FlightPosition> findAllByFlightRecord(FlightRecord flightRecord) {
        return this.flightPositionRepository.findAllByFlightRecord(flightRecord);
    }
    @Transactional
    public List<FlightPosition> findAllByFlightId(Integer flightId) {
        return this.flightPositionRepository.findByFlightRecord_Id(flightId);    }

    @Transactional
    public List<FlightPosition> findAllByDateTime(LocalDateTime dateTime) {
        return this.flightPositionRepository.findAllByTime(dateTime);
    }

    @Transactional
    public List<FlightPosition> getAllFlightPositions() {
        return this.flightPositionRepository.findAll();
    }

    public FlightPosition createNewPosition(Integer flightId, Double lat, Double lon, LocalDateTime time) {

        FlightRecord flight = flightRecordRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("FlightId not found"));

        FlightPosition pos = new FlightPosition();
        pos.setFlightRecord(flight); // Link the foreign key
        pos.setLatitude(lat);
        pos.setLongitude(lon);
        pos.setTime(time);


        return flightPositionRepository.save(pos);
    }
}
