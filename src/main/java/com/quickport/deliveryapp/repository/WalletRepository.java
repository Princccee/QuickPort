package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByPartnerId(Long partnerId);
}
