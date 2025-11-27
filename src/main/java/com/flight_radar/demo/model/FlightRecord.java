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
    private Double startLongitude;
    @Column(name = "start_location_name")
    private String startLocationName;

    @Column(name = "end_latitude")
    private Double endLatitude;
    @Column(name = "end_longitude")
    private Double endLongitude;
    @Column(name = "end_location_name")
    private String endLocationName;

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
