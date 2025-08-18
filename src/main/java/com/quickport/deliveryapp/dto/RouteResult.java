package com.quickport.deliveryapp.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RouteResult {
    private double distanceMeters;   // route distance from Mapbox
    private double durationSeconds;  // route duration
    private JsonNode geometry;       // GeoJSON geometry (LineString)
    private JsonNode raw;            // raw Mapbox response if you want extra fields

    public RouteResult() {}

    // getters/setters
    public double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }
    public double getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(double durationSeconds) { this.durationSeconds = durationSeconds; }
    public JsonNode getGeometry() { return geometry; }
    public void setGeometry(JsonNode geometry) { this.geometry = geometry; }
    public JsonNode getRaw() { return raw; }
    public void setRaw(JsonNode raw) { this.raw = raw; }
}
