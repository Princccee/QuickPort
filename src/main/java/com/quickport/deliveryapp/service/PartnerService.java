package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.*;
import com.quickport.deliveryapp.entity.*;
import com.quickport.deliveryapp.repository.*;
import com.quickport.deliveryapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartnerService {

    @Autowired DeliveryPartnerRepository deliveryPartnerRepository;
    @Autowired UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RoleRepository roleRepository;
//    @Autowired private LocationRepository locationRepository;
    @Autowired private DeliveryRequestRepository deliveryRequestRepository;
    @Autowired private JwtUtil jwtUtil;

    public PartnerRegResponse registerPartner(PartnerRegistrationRequest request){
        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Partner with this email already exists");

        // Get the partner role;
        Role partnerRole = roleRepository.findByRole(Role.RoleType.PARTNER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        //Create a user with the given name and email:
        User user = User.builder()
                .fullName(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .isVerified(false)
                .roles(Collections.singleton(partnerRole))
                .build();

        // Save the user
//        userRepository.save(user);

        // Create a vehicle entity that the partner holds
        Vehicle vehicle = Vehicle.builder()
                .type(request.getVehicleType())
                .registrationNumber(request.getVehicleRegNumber())
                .maxWeight(request.getMaxWeight())
                .build();

        // Save the vehicle entity
//        vehicleRepository.save(vehicle);

        // Create a delivery partner
        DeliveryPartner partner = DeliveryPartner.builder()
                .user(user)
                .licenceNumber(request.getLicenseNumber())
                .aadhaarNumber(request.getAadharNumber())
                .isVerified(false)
                .vehicle(vehicle)
                .build();

        // Save the entity
        deliveryPartnerRepository.save(partner);

        //Create the API response:
        PartnerRegResponse response = PartnerRegResponse.builder()
                .name(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .licenceNumber(partner.getLicenceNumber())
                .aadharNumber(partner.getAadhaarNumber())
                .vehicleRegNumber(vehicle.getRegistrationNumber())
                .build();

        return response;
    }

    public PartnerLoginResponse login(PartnerLoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return PartnerLoginResponse.builder()
                    .message("Login successful")
                    .token(jwtUtil.generateToken(request.getEmail()))
                    .build();
        }
        else throw new RuntimeException("Invalid password");
    }

    public void updateLocation(Long partnerId, LocationUpdateRequest request){
        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner doesn't exist"));

        PartnerLocation location = locationRepository.findByPartnerId(partnerId)
                .orElse(new PartnerLocation());

        location.setPartner(partner);
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());

        locationRepository.save(location);
    }

    public List<DeliveryResponse> availableRequests(Long partnerId) {
        // Check if the partner exists
        if (!deliveryPartnerRepository.findById(partnerId).isPresent()) {
            throw new RuntimeException("Partner doesn't exist");
        }

        // Get the current location of the partner
        PartnerLocation currLocation = locationRepository.findByPartnerId(partnerId)
                .orElseThrow(() -> new RuntimeException("Please update your location."));

        // TODO: Filter requests within 5 km using currLocation

        // Fetch all pending delivery requests and map them to DeliveryResponse using builder
        return deliveryRequestRepository.findByStatus(DeliveryStatus.PENDING)
                .stream()
                .map(req -> DeliveryResponse.builder()
                        .fare(req.getFare())
                        .packageDescription(req.getPackageDescription())
                        .status(String.valueOf(req.getStatus()))
                        .pickupTime(req.getPickupTime())
                        .pickupStreet(req.getPickupAddress().getStreet())
                        .pickupCity(req.getPickupAddress().getCity())
                        .dropStreet(req.getDropAddress().getStreet())
                        .dropCity(req.getDropAddress().getCity())
                        .customerName(req.getCustomer().getFullName())
                        .customerPhone(req.getCustomer().getPhone())
                        .build()
                )
                .collect(Collectors.toList());
    }

}
