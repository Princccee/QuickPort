package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.*;
import com.quickport.deliveryapp.entity.*;
import com.quickport.deliveryapp.repository.*;
import com.quickport.deliveryapp.security.JwtUtil;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PartnerService {

    @Autowired DeliveryPartnerRepository deliveryPartnerRepository;
    @Autowired UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    GeoLocationService geoLocationService;

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private DeliveryRequestRepository deliveryRequestRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private MapboxService mapboxService;

    public PartnerRegResponse registerPartner(PartnerRegistrationRequest request){

        // Search in the partner's table if the user already exists or not
        if(deliveryPartnerRepository.existsByEmail(request.getEmail())){
            log.warn("Partner already exists, please login");
            throw new RuntimeException("Partner already exists");
        }

        // Create a vehicle entity that the partner holds
        Vehicle vehicle = Vehicle.builder()
                .type(request.getVehicleType())
                .registrationNumber(request.getVehicleRegNumber())
                .maxWeight(request.getMaxWeight())
                .build();

        // Create a delivery partner
        DeliveryPartner partner = DeliveryPartner.builder()
                .fullName(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .licenceNumber(request.getLicenseNumber())
                .aadhaarNumber(request.getAadharNumber())
                .availabilityStatus(DeliveryPartner.AvailabilityStatus.AVIALABLE)
                .vehicle(vehicle)
                .build();

        log.info("Partner registered: {}", partner);

        // Save the entity
        deliveryPartnerRepository.save(partner);

        //Create the API response:
        return PartnerRegResponse.builder()
                .name(partner.getFullName())
                .email(partner.getEmail())
                .phone(partner.getPhone())
                .licenceNumber(partner.getLicenceNumber())
                .aadharNumber(partner.getAadhaarNumber())
                .vehicleRegNumber(vehicle.getRegistrationNumber())
                .build();
    }

    public PartnerLoginResponse login(PartnerLoginRequest request){
        if(!deliveryPartnerRepository.existsByEmail(request.getEmail())){
            log.warn("Partner doesn't exists, please register");
            throw new RuntimeException("Partner doesn't exists");
        }

        DeliveryPartner partner = deliveryPartnerRepository.findByEmail(request.getEmail());

        // Verify the entered password
        if(passwordEncoder.matches(request.getPassword(), partner.getPassword())){
            log.info("Password matched, partner login successful.");
            return PartnerLoginResponse.builder()
                    .message("Login successful")
                    .token(jwtUtil.generateToken(request.getEmail()))
                    .build();
        }
        else{
            log.warn("Invalid password");
            throw new RuntimeException("Invalid password");
        }
    }

    public void updateLocation(Long partnerId, LocationUpdateRequest request){
        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner doesn't exist"));

        partner.setLatitude(request.getLatitude());
        partner.setLongitude(request.getLongitude());

        deliveryPartnerRepository.save(partner); // save the updated location
    }

    public List<DeliveryResponse> availableRequests(Long partnerId) {
        // Check if the partner exists
        if (!deliveryPartnerRepository.findById(partnerId).isPresent()){
            log.info("No delivery partner available");
            throw new RuntimeException("Partner doesn't exist");
        }

        log.info("Fetch all delivery requests");
        // Get the current location of a delivery partner
        Optional<DeliveryPartner> partnerOpt = deliveryPartnerRepository.findById(partnerId);
        if (partnerOpt.isEmpty())
            throw new RuntimeException("Partner not found.");

        DeliveryPartner partner = partnerOpt.get();

        if (partner.getLatitude() == null || partner.getLongitude() == null) {
            throw new RuntimeException("Update the partner location.");
        }

        // TODO: Filter requests within 5 km using currLocation
        double RADIUS_KM = 5;

        List<DeliveryRequest> Requests = deliveryRequestRepository.findAll().stream()
                .filter(r -> r.getStatus() == DeliveryStatus.PENDING)
                .filter(r -> {
                    // Compute the distance
                    double distance = mapboxService.haversineMeters(r.getPickupAddress().getLatitude(), r.getPickupAddress().getLongitude(), partner.getLatitude(), partner.getLongitude())/1000;
                    return distance <= RADIUS_KM;
                })
                .toList();

        List<DeliveryResponse> availableRequests = new ArrayList<>();
        for(DeliveryRequest d : Requests){
            DeliveryResponse response = DeliveryResponse.builder()
                    .packageDescription(d.getPackageDescription())
                    .fare(d.getFare())
                    .pickupTime(d.getPickupTime())
                    .pickupStreet(d.getPickupAddress().getStreet())
                    .pickupCity(d.getPickupAddress().getCity())
                    .dropStreet(d.getDropAddress().getStreet())
                    .dropCity(d.getDropAddress().getCity())
                    .customerName(d.getCustomer().getFullName())
                    .customerPhone(d.getCustomer().getPhone())
                    .build();
            availableRequests.add(response);
        }

        return availableRequests;
    }

    public void acceptDelivery(Long partenerId, Long deliveryId){
        DeliveryRequest delivery = deliveryRequestRepository.findById(deliveryId)
                .orElseThrow(()-> new RuntimeException("Delivery not found."));
        if ((delivery == null))
            log.warn("Delivery not found");

        if(delivery.getStatus() != DeliveryStatus.PENDING){
            log.warn("Delivery already accepted or completed");
            throw new RuntimeException("Delivery already accepted or completed");
        }

//        User partner = userRepository.findById(partenerId)
//                .orElseThrow(() -> new RuntimeException("Delivery partner doesn't exists"));

        DeliveryPartner partner = deliveryPartnerRepository.findById(partenerId)
                    .orElseThrow(() -> new RuntimeException("Delivery partner doesn't exists"));

        delivery.setStatus(DeliveryStatus.ASSIGNED);
        delivery.setDeliveryPartner(partner);

        log.info("Delivery request accepted successfully");
        deliveryRequestRepository.save(delivery);
    }

    public void assignDelivery(Long partnerId, Long deliveryId){
        DeliveryRequest delivery = deliveryRequestRepository.findById(deliveryId)
                .orElseThrow(()-> new RuntimeException("Delivery not found"));

        DeliveryPartner partner = deliveryPartnerRepository.findById(delivery.getDeliveryPartner().getId())
                .orElseThrow(() -> new RuntimeException("Delivery partner not allocated yet"));

        if(delivery.getStatus() != DeliveryStatus.ASSIGNED)
            throw new RuntimeException("Delivery not yet assigned");

        // Update the package status
        delivery.setStatus(DeliveryStatus.IN_TRANSIT);
        deliveryRequestRepository.save(delivery);

        // Update the partner availability status:
        partner.setAvailabilityStatus(DeliveryPartner.AvailabilityStatus.ON_DELIVERY);

        log.info("Delivery request assigned successfully");
        deliveryPartnerRepository.save(partner);
    }

    public void completeDelivery(Long deliveryId){
        DeliveryRequest delivery = deliveryRequestRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery order doesn't exists"));

        if(delivery.getStatus() == DeliveryStatus.DELIVERED){
            log.warn("Package already delivered");
            throw new RuntimeException("Package Already delivered");
        }

        Long partnerId = delivery.getDeliveryPartner().getId();

        // Update the delivery status
        delivery.setStatus(DeliveryStatus.DELIVERED);
        deliveryRequestRepository.save(delivery);

        // Update the partner availability:
        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner doesn't exists"));

        partner.setAvailabilityStatus(DeliveryPartner.AvailabilityStatus.AVIALABLE);
        deliveryPartnerRepository.save(partner);

        log.info("Delivery completed successfully");
    }

}
