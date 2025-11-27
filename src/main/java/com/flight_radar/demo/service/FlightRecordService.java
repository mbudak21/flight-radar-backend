package com.flight_radar.demo.service;

import com.flight_radar.demo.model.FlightRecord;
import com.flight_radar.demo.repository.FlightRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightRecordService {
    private final  FlightRecordRepository flightRecordRepository;
    public FlightRecordService(FlightRecordRepository flightRecordRepository) {
        this.flightRecordRepository = flightRecordRepository;
    }

    @Transactional
    public List<FlightRecord> getAllFlightRecords() {
        return  flightRecordRepository.findAll();
    }

    @Transactional
    public FlightRecord getFlightRecordById(Integer flightRecordId) {
        return flightRecordRepository.findById(flightRecordId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FlightRecordId not found"));
    }

    public FlightRecord createNewFlightRecord(
                                LocalDateTime startDate,
                                double startLatitude,
                                double startLongitude,
                                String startLocationName,
                                double endLatitude,
                                double endLongitude,
                                String endLocationName){

        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setStartDate(startDate);
        flightRecord.setStartLatitude(startLatitude);
        flightRecord.setStartLongitude(startLongitude);
        flightRecord.setStartLocationName(startLocationName);
        flightRecord.setEndLatitude(endLatitude);
        flightRecord.setEndLongitude(endLongitude);
        flightRecord.setEndLocationName(endLocationName);
        return flightRecordRepository.save(flightRecord);
    }
}
