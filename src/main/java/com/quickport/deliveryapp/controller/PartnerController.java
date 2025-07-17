package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.*;
import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.DeliveryStatus;
import com.quickport.deliveryapp.entity.Role;
import com.quickport.deliveryapp.repository.DeliveryRequestRepository;
import com.quickport.deliveryapp.service.DeliveryRequestService;
import com.quickport.deliveryapp.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partner")
public class PartnerController {

    @Autowired PartnerService partnerService;
    @Autowired
    DeliveryRequestRepository deliveryRequestRepository;

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

    @GetMapping("/{id}/available-requests")
    public ResponseEntity<?> getAvailableRequests(@PathVariable Long id){
        List<DeliveryResponse> availableRequests = partnerService.availableRequests(id);

        return ResponseEntity.ok(availableRequests);
    }

    @PostMapping("/{partnerId}/accept/{deliveryId}")
    public ResponseEntity<?> acceptDelivery(@PathVariable Long partnerId,
                                            @PathVariable Long deliveryId) {
        partnerService.acceptDelivery(partnerId, deliveryId);
        return ResponseEntity.ok("Delivery accepted");
    }

    @PostMapping("/{partnerId}/assign/{deliveryId}")
    public ResponseEntity<?> assignDelivery(@PathVariable Long partnerId, @PathVariable Long deliveryId){

        partnerService.assignDelivery(partnerId, deliveryId);
        return ResponseEntity.ok("Delivery assigned successfully");
    }
}
