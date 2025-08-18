package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.entity.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;

@Service
@RequiredArgsConstructor
public class GeoLocationService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.maps.api.key}")
    private String apiKey;

    // Convert the address object into a string address
    public String buildFullAddress(Address address) {
        // Build full string for geocoding
        StringBuilder builder = new StringBuilder();
        if (address.getStreet() != null) builder.append(address.getStreet()).append(", ");
        if (address.getLandmark() != null) builder.append(address.getLandmark()).append(", ");
        if (address.getCity() != null) builder.append(address.getCity()).append(", ");
        if (address.getState() != null) builder.append(address.getState()).append(", ");
        if (address.getPostalCode() != null) builder.append(address.getPostalCode());

        return builder.toString().trim().replaceAll(",\\s*$", ""); // clean trailing commas
    }

    // Convert the address into real coordinates
    public double[] getLatLongFromAddress(Address address) {

        String fullAddress = buildFullAddress(address);

        String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                fullAddress.replace(" ", "+"),
                apiKey
        );

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());

        if (!json.getString("status").equals("OK")) {
            throw new RuntimeException("Failed to geocode address: " + json.getString("status"));
        }

        JSONObject location = json.getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");

        return new double[] {
                location.getDouble("lat"),
                location.getDouble("lng")
        };
    }

    // get the real distance between two given coordinates
    public double getRealDistanceInKm(double[] origin, double[] destination) {
        String originStr = origin[0] + "," + origin[1];
        String destStr = destination[0] + "," + destination[1];

        String url = String.format(
                "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
                originStr, destStr, apiKey
        );

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());

        if (!json.getString("status").equals("OK")) {
            throw new RuntimeException("Failed to get directions: " + json.getString("status"));
        }

        JSONArray routes = json.getJSONArray("routes");
        JSONObject leg = routes.getJSONObject(0)
                .getJSONArray("legs")
                .getJSONObject(0);

        double distanceMeters = leg.getJSONObject("distance").getDouble("value");

        return distanceMeters / 1000.0; // convert to KM
    }
}
