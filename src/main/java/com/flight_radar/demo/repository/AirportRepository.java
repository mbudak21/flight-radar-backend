package com.flight_radar.demo.repository;

import com.flight_radar.demo.model.Airport;
import com.flight_radar.demo.model.FlightPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer> {
    boolean existsById(Integer id);
}
