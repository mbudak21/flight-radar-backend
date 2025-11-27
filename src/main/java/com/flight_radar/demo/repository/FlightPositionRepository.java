package com.flight_radar.demo.repository;

import com.flight_radar.demo.model.FlightPosition;
import com.flight_radar.demo.model.FlightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightPositionRepository extends JpaRepository<FlightPosition, Integer> {
    boolean existsById(Integer id);

    List<FlightPosition> findAllByFlightRecord(FlightRecord flightRecord);

    List<FlightPosition> findByFlightRecord_Id(Integer id);

    List<FlightPosition> findAllByTime(LocalDateTime time);
}
