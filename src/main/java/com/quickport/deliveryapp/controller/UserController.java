package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.AddressDTO;
import com.quickport.deliveryapp.dto.Rate_Revie_Request;
import com.quickport.deliveryapp.entity.Address;
import com.quickport.deliveryapp.entity.RatingReview;
import com.quickport.deliveryapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired private UserService userService;

    @PostMapping("/addPickupAddress")
    public ResponseEntity<Address> addPickupAddress(@RequestBody AddressDTO address){
        log.info("Add pickup address");
        Address pickupAddress = userService.createAddress(address);
        return ResponseEntity.ok(pickupAddress);
    }

    @PostMapping("/addDropAddress")
    public ResponseEntity<Address> addDropAddress(@RequestBody AddressDTO address){
        log.info("Add drop location");
        Address dropAddress = userService.createAddress(address);
        return ResponseEntity.ok(dropAddress);
    }

    @PostMapping("/rate-review")
    public ResponseEntity<RatingReview> rateRide(@RequestBody Rate_Revie_Request request){
        log.info("Rate a ride initiated");
        RatingReview ratingReview = userService.rate_review_ride(request);

        return ResponseEntity.ok(ratingReview);
    }
}
