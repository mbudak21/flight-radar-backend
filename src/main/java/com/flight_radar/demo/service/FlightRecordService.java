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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private List<FlightPosition> downsample(List<FlightPosition> list, int maxSize) {
        int n = list.size();
        if (n <= maxSize) return list;

        int last = n - 1;
        double step = (double) last / (maxSize - 1);

        List<FlightPosition> out = new ArrayList<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            int idx = (int) Math.round(i * step);
            out.add(list.get(idx));
        }
        return out;
    }

    public List<FlightPositionDTO> getAllFlightPositionsByDate(Integer flightId, LocalDateTime date, Integer maxSize){
        if(maxSize == null || maxSize <= 1) {
            throw new IllegalArgumentException("maxSize must be >= 2");
        }

        FlightRecord record = flightRecordRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found: " + flightId));

        // Get positions from repo
        List<FlightPosition> positions = flightPositionRepository.findAllByFlightRecordAndTimeLessThanEqualOrderByTimeDesc(record, date);

        if (positions.size() > maxSize) {
            positions = downsample(positions, maxSize);
        }

        // Convert to DTO
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
                        // No position yet at this time -> skip flight
                        return null;
                    }
                    LocalDateTime lastUpdatedAt = current.getTime();

                    double latDiff = Math.abs(record.getEndLatitude() - current.getLatitude());
                    double lngDiff = Math.abs(record.getEndLongitude() - current.getLongitude());

//                    if (current.getTime() - dateTime
////                        && latDiff < 0.01 && lngDiff < 0.01
//                    ) {
//                        return null;
//                    }

                    if (current.getTime().isBefore(dateTime.minusMinutes(5))){
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

                            // LastUpdatedAt:
                            lastUpdatedAt,

                            // Departure Time
                            record.getStartDate(),

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
