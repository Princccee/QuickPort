package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.PartnerLoginRequest;
import com.quickport.deliveryapp.dto.PartnerLoginResponse;
import com.quickport.deliveryapp.dto.PartnerRegResponse;
import com.quickport.deliveryapp.dto.PartnerRegistrationRequest;
import com.quickport.deliveryapp.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {

    @Autowired PartnerService partnerService;

    @PostMapping("/register")
    public ResponseEntity<PartnerRegResponse> register(@RequestBody PartnerRegistrationRequest request){
        PartnerRegResponse partner =  partnerService.registerPartner(request);
        return ResponseEntity.ok(partner);
    }

    @PostMapping("/login")
    public PartnerLoginResponse login(@RequestBody PartnerLoginRequest request){
        return partnerService.login(request);
    }
}
