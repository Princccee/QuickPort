package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentReferenceId(String referenceId);
}
