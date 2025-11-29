package com.flight_radar.demo.service;

import com.flight_radar.demo.dto.FlightPositionDTO;
import com.flight_radar.demo.model.FlightPosition;
import com.flight_radar.demo.model.FlightRecord;
import com.flight_radar.demo.repository.FlightPositionRepository;
import com.flight_radar.demo.repository.FlightRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.flight_radar.demo.dto.FlightDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class FlightRecordService {
    private final  FlightRecordRepository flightRecordRepository;
    private final FlightPositionRepository flightPositionRepository;

    public FlightRecordService(FlightRecordRepository flightRecordRepository,  FlightPositionRepository flightPositionRepository) {
        this.flightRecordRepository = flightRecordRepository;
        this.flightPositionRepository = flightPositionRepository;
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
                                double startLatitude,
                                double startLongitude,
                                String startLocationName,
                                double endLatitude,
                                double endLongitude,
                                String endLocationName){

        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setStartLatitude(startLatitude);
        flightRecord.setStartLongitude(startLongitude);
        flightRecord.setStartLocationName(startLocationName);
        flightRecord.setEndLatitude(endLatitude);
        flightRecord.setEndLongitude(endLongitude);
        flightRecord.setEndLocationName(endLocationName);
        return flightRecordRepository.save(flightRecord);
    }

    public List<FlightPositionDTO> getAllFlightPositionsByDate(Integer flightId, LocalDateTime date){
        FlightRecord record = flightRecordRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found: " + flightId));

        List<FlightPosition> positions = flightPositionRepository.findAllByFlightRecordAndTimeLessThanEqualOrderByTimeDesc(record, date);
        return positions.stream()
                .map(p -> new FlightPositionDTO(p.getLatitude(), p.getLongitude(), p.getTime()))
                .toList();

    }



    public List<FlightDTO> findAllByDateTime(LocalDateTime dateTime) {
        List<FlightRecord> records = flightRecordRepository.findAll();

        return records.stream()
                .map(record -> {
                    // last 2 positions up to given time
                    List<FlightPosition> positions =
                            flightPositionRepository
                                    .findTop2ByFlightRecordAndTimeLessThanEqualOrderByTimeDesc(
                                            record, dateTime
                                    );

                    FlightPosition current = positions.isEmpty() ? null : positions.get(0);
                    FlightPosition prev = positions.size() > 1 ? positions.get(1) : null;

                    if (current == null) {
                        // No position yet at this time -> skip this flight
                        return null;
                    }

                    return new FlightDTO(
                            record.getId(),
                            record.getStartLatitude(),
                            record.getStartLongitude(),
                            record.getStartLocationName(),

                            record.getEndLatitude(),
                            record.getEndLongitude(),
                            record.getEndLocationName(),

                            prev != null ?  prev.getTime(): current.getTime(),

                            current.getLatitude(),
                            current.getLongitude(),

                            prev != null ? prev.getLatitude() : null,
                            prev != null ? prev.getLongitude() : null
                    );
                })
                .filter(Objects::nonNull) // drop flights with no position yet
                .toList();
    }

}
