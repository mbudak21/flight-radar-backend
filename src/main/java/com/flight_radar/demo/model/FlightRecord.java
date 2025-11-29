package com.flight_radar.demo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FlightRecord")
public class FlightRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Formula("(SELECT MIN(fp.time) FROM flight_position fp WHERE fp.flight_ID = ID)")
    private LocalDateTime startDate;

    @Column(name = "start_latitude")
    private Double startLatitude;
    @Column(name = "start_longitude")
    private Double startLongitude;
    @Column(name = "start_location_name")
    private String startLocationName;

    @Column(name = "end_latitude")
    private Double endLatitude;
    @Column(name = "end_longitude")
    private Double endLongitude;
    @Column(name = "end_location_name")
    private String endLocationName;

    @JsonIgnore
    @OneToMany(
            mappedBy = "flightRecord",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<FlightPosition> positions = new ArrayList<>();

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void addPosition(FlightPosition position) {
        positions.add(position);
        position.setFlightRecord(this);
    }

    public void removePosition(FlightPosition position) {
        positions.remove(position);
        position.setFlightRecord(null);
    }

    public List<FlightPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<FlightPosition> positions) {
        this.positions = positions;
    }

    public Double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public Double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartLocationName() {
        return startLocationName;
    }

    public void setStartLocationName(String startLocationName) {
        this.startLocationName = startLocationName;
    }

    public Double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public Double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getEndLocationName() {
        return endLocationName;
    }

    public void setEndLocationName(String endLocationName) {
        this.endLocationName = endLocationName;
    }

//    public List<FlightPosition> getPositions() {
//        return positions;
//    }
//    public void setPositions(List<FlightPosition> positions) {
//        this.positions = positions;
//    }
}
