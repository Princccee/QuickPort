package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.SignupRequest;
import com.quickport.deliveryapp.entity.Role;
import com.quickport.deliveryapp.entity.User;
import com.quickport.deliveryapp.repository.RoleRepository;
import com.quickport.deliveryapp.repository.UserRepository;
import com.quickport.deliveryapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;


    public User registerCustomer(SignupRequest request){
        // Check if the email is previously registered
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email already registered");

        Role customerRole = roleRepository.findByRole(Role.RoleType.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Create a new user record
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(customerRole))
                .isVerified(false)
                .build();

        return userRepository.save(user);
    }

    public String loginUser(String email, String password){
        // Check if the user already exists in the DB or not
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));

        // Then math the given password with the one stored in encoded form in the DB
        if(passwordEncoder.matches(password, user.getPassword()))
            return jwtUtil.generateToken(email);
        else throw new RuntimeException("Invalid password");
    }


}
