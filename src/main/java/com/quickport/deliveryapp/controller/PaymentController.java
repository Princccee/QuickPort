package com.quickport.deliveryapp.controller;

import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.Payment;
import com.quickport.deliveryapp.entity.PaymentStatus;
import com.quickport.deliveryapp.repository.DeliveryRequestRepository;
import com.quickport.deliveryapp.repository.PaymentRepository;
import com.razorpay.Order;
import com.quickport.deliveryapp.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DeliveryRequestRepository deliveryRequestRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create-order")
    public Order createOrder(@RequestParam int amount, @RequestParam String receipt) {
        try {
            Order order = paymentService.createOrder(amount, "INR", receipt);
            return order; // send to frontend
        }
        catch (Exception e) {
            return null;
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
