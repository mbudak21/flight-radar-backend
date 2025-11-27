package com.flight_radar.demo.repository;

import com.flight_radar.demo.model.FlightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRecordRepository extends JpaRepository<FlightRecord, Integer> {
    boolean existsById(Integer id);
}
