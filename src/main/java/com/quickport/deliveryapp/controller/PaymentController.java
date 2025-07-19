package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.dto.CreatePaymentRequest;
import com.quickport.deliveryapp.entity.Payment;
import com.quickport.deliveryapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(@RequestBody CreatePaymentRequest request) {
        Payment payment = paymentService.createPayment(request.getDeliveryRequestId(), request.getAmount());
        return ResponseEntity.ok(payment);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
//        return ResponseEntity.ok(paymentService.getPaymentById(id));
//    }
//
//    @GetMapping("/partner/{partnerId}")
//    public ResponseEntity<List<Payment>> getPaymentsByPartner(@PathVariable Long partnerId) {
//        return ResponseEntity.ok(paymentService.getPaymentsByPartner(partnerId));
//    }
}
