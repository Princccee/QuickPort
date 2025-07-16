package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, Long> {
    List<DeliveryRequest> findByCustomerId(Long customerId);
    List<DeliveryRequest> findByDeliveryPartnerId(Long partnerId);
    List<DeliveryRequest> findByStatus(DeliveryStatus status);
}
