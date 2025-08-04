package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.AddressDTO;
import com.quickport.deliveryapp.entity.Address;
import com.quickport.deliveryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired private UserService userService;

    @PostMapping("/addPickupAddress")
    public ResponseEntity<Address> addPickupAddress(@RequestBody AddressDTO address){
        Address pickupAddress = userService.createAddress(address);
        return ResponseEntity.ok(pickupAddress);
    }

    @PostMapping("/addDropAddress")
    public ResponseEntity<Address> addDropAddress(@RequestBody AddressDTO address){
        Address dropAddress = userService.createAddress(address);
        return ResponseEntity.ok(dropAddress);
    }
}
