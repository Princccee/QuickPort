package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.DeliveryRequestDTO;
import com.quickport.deliveryapp.dto.DeliveryResponse;
import com.quickport.deliveryapp.service.DeliveryRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@Slf4j
public class DeliveryController {

    @Autowired private DeliveryRequestService deliveryRequestService;

    // For now we are passing the customer ID manually, later will extract from JWT auth token
    @PostMapping("/book")
    public ResponseEntity<DeliveryResponse> bookDelivery(@RequestBody DeliveryRequestDTO request, @RequestParam Long customerId){
        log.info("Raise a delivery request");
        DeliveryResponse deliveryorder = deliveryRequestService.createDeliveryRequest(request, customerId);
        return ResponseEntity.ok(deliveryorder);
    }

    @GetMapping("/my")
    public ResponseEntity<List<DeliveryResponse>> getAllDeliveryOrders(@RequestParam Long customerId){
        log.info("Fetch order history");
        return ResponseEntity.ok(deliveryRequestService.getDeliveriesForCustomer(customerId));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<DeliveryResponse> getDeliveryStatus(@PathVariable Long id){
        log.info("Get the current status of a delivery package.");
        return deliveryRequestService.getStatus(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
