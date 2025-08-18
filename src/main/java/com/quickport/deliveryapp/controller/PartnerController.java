package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.*;
import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.DeliveryStatus;
import com.quickport.deliveryapp.entity.Wallet;
import com.quickport.deliveryapp.repository.DeliveryRequestRepository;
import com.quickport.deliveryapp.repository.WalletRepository;
import com.quickport.deliveryapp.service.DeliveryRequestService;
import com.quickport.deliveryapp.service.PartnerService;
import com.quickport.deliveryapp.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partner")
@Slf4j
public class PartnerController {

    @Autowired PartnerService partnerService;
    @Autowired DeliveryRequestRepository deliveryRequestRepository;
    @Autowired
    WalletService walletService;

    // Register the delivery partner
    @PostMapping("/register")
    public ResponseEntity<PartnerRegResponse> register(@RequestBody PartnerRegistrationRequest request){
        PartnerRegResponse partner =  partnerService.registerPartner(request);
        log.info("Register delivery partner with : {}", request);
        return ResponseEntity.ok(partner);
    }

    // Login the delivery partner
    @PostMapping("/login")
    public PartnerLoginResponse login(@RequestBody PartnerLoginRequest request){
        return partnerService.login(request);
    }

    // Update the partner location
    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody LocationUpdateRequest request) {
        partnerService.updateLocation(id, request);
        return ResponseEntity.ok("Location updated");
    }

    // See all the delivery request generated nearby
    @GetMapping("/{id}/available-requests")
    public ResponseEntity<?> getAvailableRequests(@PathVariable Long id){
        List<DeliveryResponse> availableRequests = partnerService.availableRequests(id);

        return ResponseEntity.ok(availableRequests);
    }

    // Accept a delivery order
    @PostMapping("/{partnerId}/accept/{deliveryId}")
    public ResponseEntity<?> acceptDelivery(@PathVariable Long partnerId,
                                            @PathVariable Long deliveryId) {
        partnerService.acceptDelivery(partnerId, deliveryId);
        return ResponseEntity.ok("Delivery accepted");
    }

    // Assign a delivery package to a delivery partner
    @PostMapping("/{partnerId}/assign/{deliveryId}")
    public ResponseEntity<?> assignDelivery(@PathVariable Long partnerId, @PathVariable Long deliveryId){
        partnerService.assignDelivery(partnerId, deliveryId);
        return ResponseEntity.ok("Delivery assigned successfully");
    }

    @PostMapping("/{deliveryId}/complete")
    public ResponseEntity<?> completeDelivery(@PathVariable Long deliveryId){
        partnerService.completeDelivery(deliveryId);
        return ResponseEntity.ok("Delivery marked as completed");
    }

    @PostMapping("/register-wallet")
    public ResponseEntity<?> addWallet(@RequestBody WalletRegister request){
//        System.out.println("Raw JSON: " + request);
        Wallet wallet = walletService.registerWallet(request);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{walletId}/checkBalance")
    public ResponseEntity<?> checkBalance(@PathVariable Long walletId){
        Double currBalance = walletService.checkWalletBalance(walletId);
        return ResponseEntity.ok(currBalance);
    }
}
