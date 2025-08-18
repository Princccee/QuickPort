package com.quickport.deliveryapp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GeocodeResult {
    private double latitude;
    private double longitude;
    private String placeName;

    public GeocodeResult() {}

    public GeocodeResult(double latitude, double longitude, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
    }

//    // getters + setters
//    public double getLatitude() { return latitude; }
//    public void setLatitude(double latitude) { this.latitude = latitude; }
//    public double getLongitude() { return longitude; }
//    public void setLongitude(double longitude) { this.longitude = longitude; }
//    public String getPlaceName() { return placeName; }
//    public void setPlaceName(String placeName) { this.placeName = placeName; }
}
