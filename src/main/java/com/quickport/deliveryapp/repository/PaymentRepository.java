package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentReferenceId(String referenceId);

//    List<Payment> findByDeliveryPartnerId(Long partnerId);
}
