package com.quickport.deliveryapp.util;

import com.quickport.deliveryapp.entity.Address;
import com.quickport.deliveryapp.entity.DeliveryPartner;
import com.quickport.deliveryapp.entity.PartnerLocation;
import com.quickport.deliveryapp.repository.DeliveryPartnerRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryOrderUtils {
    private static final int EARTH_RADIUS_KM = 6371;

    @Autowired private DeliveryPartnerRepository deliveryPartnerRepository;

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // returns distance in kilometers
    }

    public double computeDeliveryCost(double distance) {
        double baseFare = 50.0;
        double perKmRate = 5.0;

        return baseFare + (distance * perKmRate);
    }

    public List<DeliveryPartner> findNearbyPartners(Address pickupLocation, List<DeliveryPartner> allPartners) {
        return allPartners.stream()
                .filter(partner -> {
                    PartnerLocation partnerLocation = partner.getLocation(); // partner's live location coordinates
                    double distance = calculateDistance(
                            pickupLocation.getLatitude(), pickupLocation.getLongitude(),
                            partnerLocation.getLatitude(), partnerLocation.getLongitude()
                    );
                    return distance <= 5.0; // within 5 km
                })
                .collect(Collectors.toList());
    }

}
