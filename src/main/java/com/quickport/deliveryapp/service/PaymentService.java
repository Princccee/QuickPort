package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.Payment;
import com.quickport.deliveryapp.entity.PaymentStatus;
import com.quickport.deliveryapp.repository.DeliveryRequestRepository;
import com.quickport.deliveryapp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private DeliveryRequestRepository deliveryRequestRepository;

    public Payment createPayment(Long deliveryRequestId, Double amount) {
        DeliveryRequest deliveryRequest = deliveryRequestRepository.findById(deliveryRequestId)
                .orElseThrow(() -> new RuntimeException("DeliveryRequest not found"));

        Payment payment = Payment.builder()
                .delivery(deliveryRequest)
                .amount(amount)
                .paymentTime(LocalDateTime.now())
                .status(PaymentStatus.INITIATED) // assume immediate success for now
                .build();

        return paymentRepository.save(payment);
    }

//    public List<Payment> getPaymentsByPartner(Long partnerId) {
//        return paymentRepository.findByDeliveryPartnerId(partnerId);
//    }
//
//    public Payment getPaymentById(Long id) {
//        return paymentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//    }
}
