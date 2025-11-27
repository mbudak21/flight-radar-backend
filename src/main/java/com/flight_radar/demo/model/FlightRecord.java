package com.flight_radar.demo.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "FlightRecord")
public class FlightRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "start_latitude")
    private Double startLatitude;
    @Column(name = "start_longitude")
    private Double StartLongitude;
    @Column(name = "start_location_name")
    private String StartLocationName;

    @Column(name = "end_latitude")
    private Double EndLatitude;
    @Column(name = "end_longitude")
    private Double EndLongitude;
    @Column(name = "end_location_name")
    private String EndLocationName;

//    @OneToMany(mappedBy = "FlightRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<FlightPosition> positions;

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
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public Double getStartLatitude() {
        return startLatitude;
    }
    public void setStartLatitude(Double startLatitude) {
        startLatitude = startLatitude;
    }
    public Double getStartLongitude() {
        return StartLongitude;
    }
    public void setStartLongitude(Double startLongitude) {
        StartLongitude = startLongitude;
    }
    public String getStartLocationName() {
        return StartLocationName;
    }
    public void setStartLocationName(String startLocationName) {
        StartLocationName = startLocationName;
    }
    public Double getEndLatitude() {
        return EndLatitude;
    }
    public void setEndLatitude(Double endLatitude) {
        EndLatitude = endLatitude;
    }
    public Double getEndLongitude() {
        return EndLongitude;
    }
    public void setEndLongitude(Double endLongitude) {
        EndLongitude = endLongitude;
    }
    public String getEndLocationName() {
        return EndLocationName;
    }
    public void setEndLocationName(String endLocationName) {
        EndLocationName = endLocationName;
    }
//    public List<FlightPosition> getPositions() {
//        return positions;
//    }
//    public void setPositions(List<FlightPosition> positions) {
//        this.positions = positions;
//    }
}
