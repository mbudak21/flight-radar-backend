package com.flight_radar.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "flight_position", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"flight_ID", "time"})
})
public class FlightPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_ID", nullable = false)
    @JsonIgnore
    private FlightRecord flightRecord;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    // Getters & Setters

    public Integer getId() { return id; }
    public void setId(Integer id) {
        this.id = id;
    }

    public FlightRecord getFlightRecord() {
        return flightRecord;
    }
    public void setFlightRecord(FlightRecord flightRecord) {
        this.flightRecord = flightRecord;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
