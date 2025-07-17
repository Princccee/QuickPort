package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.*;
import com.quickport.deliveryapp.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody LocationUpdateRequest request) {
        partnerService.updateLocation(id, request);
        return ResponseEntity.ok("Location updated");
    }
}
