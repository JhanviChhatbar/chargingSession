package com.orenda.charging_session.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationId;
    private String driverToken;
    private String status;

    private LocalDateTime timestamp;

    public Decision() {}

    public Decision(String stationId, String driverToken, String status) {
        this.stationId = stationId;
        this.driverToken = driverToken;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getDriverToken() {
        return driverToken;
    }

    public void setDriverToken(String driverToken) {
        this.driverToken = driverToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

