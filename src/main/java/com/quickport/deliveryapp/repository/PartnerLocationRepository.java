package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.PartnerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerLocationRepository extends JpaRepository<PartnerLocation, Long> {
    PartnerLocation findByPartnerId(Long partnerId);
}
