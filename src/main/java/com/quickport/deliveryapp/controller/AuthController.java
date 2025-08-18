package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.LoginRequest;
import com.quickport.deliveryapp.dto.LoginResponse;
import com.quickport.deliveryapp.dto.SignupRequest;
import com.quickport.deliveryapp.entity.User;
import com.quickport.deliveryapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
//@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody SignupRequest request){
        log.info("Register user with : {} ", request);
        User user = userService.registerCustomer(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        log.info("Login user with : {}", request);
        return userService.loginUser(request.getEmail(), request.getPassword());
    }
}
