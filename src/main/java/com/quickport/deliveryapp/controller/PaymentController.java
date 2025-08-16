package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.Payment;
import com.quickport.deliveryapp.entity.PaymentStatus;
import com.quickport.deliveryapp.repository.DeliveryRequestRepository;
import com.quickport.deliveryapp.repository.PaymentRepository;
import com.razorpay.Order;
import com.quickport.deliveryapp.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DeliveryRequestRepository deliveryRequestRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestParam int amount, @RequestParam String receipt) {
        try {
            log.info("Payment order initiated");
            Order order = paymentService.createOrder(amount, "INR", receipt);

            Map<String, Object> response = new HashMap<>();
            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("receipt", order.get("receipt"));
            response.put("status", order.get("status"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public String verifyPayment(@RequestParam String orderId,
                                @RequestParam String paymentId,
                                @RequestParam String signature) {
        boolean isValid = paymentService.verifyPayment(orderId, paymentId, signature);

        if (isValid) {
            // ✅ Save to DB
            Payment payment = new Payment();
            payment.setPaymentReferenceId(paymentId);
            payment.setStatus(PaymentStatus.SUCCESS);
//            payment.setPaymentReferenceId();

            paymentRepository.save(payment);

            return "{\"status\":\"success\"}";
        } else {
            return "{\"status\":\"failure\"}";
        }
    }
}
