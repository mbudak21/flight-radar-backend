package com.flight_radar.demo.service;

import com.flight_radar.demo.model.Airport;
import com.flight_radar.demo.model.FlightPosition;
import com.flight_radar.demo.repository.AirportRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ReadOnlyMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {
    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Transactional
    public List<Airport> findAll() {
        return airportRepository.findAll();
    }
}
