package com.quickport.deliveryapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickport.deliveryapp.dto.GeocodeResult;
import com.quickport.deliveryapp.dto.RouteResult;
import com.quickport.deliveryapp.entity.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MapboxService {

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${mapbox.token}")
    private String token;

    public MapboxService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.mapbox.com")
                .build();
    }

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

    /**
     * Forward geocode: address -> lat,lon (returns first result)
     */
    public GeocodeResult forwardGeocode(Address address) {
        String strAddress = buildFullAddress(address);
        String encoded = strAddress.replace(" ", "+"); // basic encoding; better to URLEncoder.encode in production
        JsonNode resp = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geocoding/v5/mapbox.places/{query}.json")
                        .queryParam("access_token", token)
                        .queryParam("limit", 1)
                        .build(encoded))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (resp == null || !resp.has("features") || resp.get("features").size() == 0) {
            return null;
        }

        JsonNode first = resp.get("features").get(0);
        JsonNode center = first.get("center"); // [lon, lat]
        double lon = center.get(0).asDouble();
        double lat = center.get(1).asDouble();
        String placeName = first.has("place_name") ? first.get("place_name").asText() : "";
        return new GeocodeResult(lat, lon, placeName);
    }

    /**
     * Reverse geocode: lat,lon -> address (returns first result)
     */
    public GeocodeResult reverseGeocode(double latitude, double longitude) {
        String coord = String.format("%f,%f", longitude, latitude); // note order: lon,lat
        JsonNode resp = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geocoding/v5/mapbox.places/{coord}.json")
                        .queryParam("access_token", token)
                        .queryParam("limit", 1)
                        .build(coord))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (resp == null || !resp.has("features") || resp.get("features").size() == 0) {
            return null;
        }
        JsonNode first = resp.get("features").get(0);
        JsonNode center = first.get("center"); // [lon, lat]
        double lon = center.get(0).asDouble();
        double lat = center.get(1).asDouble();
        String placeName = first.has("place_name") ? first.get("place_name").asText() : "";
        return new GeocodeResult(lat, lon, placeName);
    }

    /**
     * Directions: origin -> destination using profile (driving, driving-traffic, walking, cycling)
     * Coordinates order in path: lon,lat; return RouteResult with distance/duration and GeoJSON geometry
     */
    public RouteResult getDirections(double originLat, double originLon,
                                     double destLat, double destLon,
                                     String profile) {
        String coords = String.format("%f,%f;%f,%f", originLon, originLat, destLon, destLat);
        String profilePath = (profile == null || profile.isBlank()) ? "driving" : profile;

        JsonNode resp = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/directions/v5/mapbox/" + profilePath + "/" + coords)
                        .queryParam("access_token", token)
                        .queryParam("geometries", "geojson")
                        .queryParam("overview", "full")
                        .queryParam("steps", "true")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (resp == null || !resp.has("routes") || resp.get("routes").size() == 0) {
            return null;
        }
        JsonNode route = resp.get("routes").get(0);
        RouteResult r = new RouteResult();
        r.setDistanceMeters(route.has("distance") ? route.get("distance").asDouble() : 0.0);
        r.setDurationSeconds(route.has("duration") ? route.get("duration").asDouble() : 0.0);
        r.setGeometry(route.path("geometry")); // GeoJSON geometry (LineString)
        r.setRaw(resp);
        return r;
    }

    /**
     * Straight-line distance (Haversine) in meters
     */
    public double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}
