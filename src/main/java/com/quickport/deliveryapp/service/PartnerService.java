package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.PartnerLoginRequest;
import com.quickport.deliveryapp.dto.PartnerLoginResponse;
import com.quickport.deliveryapp.dto.PartnerRegResponse;
import com.quickport.deliveryapp.dto.PartnerRegistrationRequest;
import com.quickport.deliveryapp.entity.DeliveryPartner;
import com.quickport.deliveryapp.entity.Role;
import com.quickport.deliveryapp.entity.User;
import com.quickport.deliveryapp.entity.Vehicle;
import com.quickport.deliveryapp.repository.DeliveryPartnerRepository;
import com.quickport.deliveryapp.repository.RoleRepository;
import com.quickport.deliveryapp.repository.UserRepository;
import com.quickport.deliveryapp.repository.VehicleRepository;
import com.quickport.deliveryapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class PartnerService {

    @Autowired DeliveryPartnerRepository deliveryPartnerRepository;
    @Autowired UserRepository userRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RoleRepository roleRepository;
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
}
