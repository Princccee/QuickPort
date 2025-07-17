package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.PartnerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<PartnerLocation, Long> {
    Optional<PartnerLocation> findByPartnerId(Long id);
}
