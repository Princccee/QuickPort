package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.DeliveryRequestDTO;
import com.quickport.deliveryapp.dto.DeliveryResponse;
import com.quickport.deliveryapp.service.DeliveryRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired private DeliveryRequestService deliveryRequestService;

    // For now we are passing the customer ID manually, later will extract from JWT auth token
    @PostMapping("/book")
    public ResponseEntity<DeliveryResponse> bookDelivery(@RequestBody DeliveryRequestDTO request, @RequestParam Long customerId){
        DeliveryResponse deliveryorder = deliveryRequestService.createDeliveryRequest(request, customerId);
        return ResponseEntity.ok(deliveryorder);
    }

    @GetMapping("/my")
    public ResponseEntity<List<DeliveryResponse>> getAllDeliveryOrders(@RequestParam Long customerId){
        return ResponseEntity.ok(deliveryRequestService.getDeliveriesForCustomer(customerId));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<DeliveryResponse> getDeliveryStatus(@PathVariable Long id){
        return deliveryRequestService.getStatus(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
