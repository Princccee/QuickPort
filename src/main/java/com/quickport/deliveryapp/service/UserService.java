package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.AddressDTO;
import com.quickport.deliveryapp.dto.LoginResponse;
import com.quickport.deliveryapp.dto.SignupRequest;
import com.quickport.deliveryapp.entity.Address;
import com.quickport.deliveryapp.entity.Role;
import com.quickport.deliveryapp.entity.Roles;
import com.quickport.deliveryapp.entity.User;
import com.quickport.deliveryapp.repository.AddressRepository;
import com.quickport.deliveryapp.repository.RoleRepository;
import com.quickport.deliveryapp.repository.UserRepository;
import com.quickport.deliveryapp.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private GeoLocationService geoLocationService;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public User registerCustomer(SignupRequest request){
        // Check if the email is previously registered
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            log.warn("User with email {} already exists.", request.getEmail());
            throw new RuntimeException("Email already registered");
        }

        log.info("Creating a new user");
        // Create a new user record
        User user = User.builder()
                .fullName(request.getFullName()) // full name
                .email(request.getEmail()) // user email
                .phone(request.getPhone()) // user's phone number
                .password(passwordEncoder.encode(request.getPassword())) // encode the user's password
                .role(Roles.CUSTOMER) // role as : CUSTOMER
                .isVerified(false)
                .build();

        log.info("User successfully registered");
        return userRepository.save(user);
    }

    public LoginResponse loginUser(String email, String password){
        // Check if the user already exists in the DB or not
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        if(user == null)
            log.warn("User doesn't exists please register");

        // Then math the given password with the one stored in encoded form in the DB
        if(passwordEncoder.matches(password, user.getPassword())){
            log.info("Password matched and login successful");
            return LoginResponse.builder()
                    .message("User logged in successfully.")
                    .jwtToken(jwtUtil.generateToken(email))
                    .build();
        }
        else{
            log.warn("Invalid password entered");
            throw new RuntimeException("Invalid password");
        }
    }

    public Address createAddress(AddressDTO request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
        if(user == null)
            log.warn("User doesn't exists");

        //Create a new address record:
        Address address = Address.builder()
                .user(user)
                .street(request.getStreet())
                .landmark(request.getLandmark())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .build();

//        double[] coordinates = geoLocationService.getLatLongFromAddress(address);

        //Add the latitude and longitude:
//        address.setLatitude(coordinates[0]);
//        address.setLongitude(coordinates[1]);

        log.info("Address added : {} ", address);
        return addressRepository.save(address);
    }

}
