package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {
    Optional<DeliveryPartner> findById(Long Id);
    DeliveryPartner findByEmail(String email);
    Boolean existsByEmail(String email);

//    boolean existsByEmail(String email);
}
