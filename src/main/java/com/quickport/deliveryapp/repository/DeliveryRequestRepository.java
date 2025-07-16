package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.DeliveryRequest;
import com.quickport.deliveryapp.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRequestRepository extends JpaRepository<DeliveryRequest, Long> {
    List<DeliveryRequest> findByCustomerId(Long customerId);
    List<DeliveryRequest> findByDeliveryPartnerId(Long partnerId);
    List<DeliveryRequest> findByStatus(DeliveryStatus status);
}
