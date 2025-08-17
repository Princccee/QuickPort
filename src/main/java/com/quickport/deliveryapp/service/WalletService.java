package com.quickport.deliveryapp.service;

import com.quickport.deliveryapp.dto.WalletRegister;
import com.quickport.deliveryapp.entity.DeliveryPartner;
import com.quickport.deliveryapp.entity.Wallet;
import com.quickport.deliveryapp.repository.DeliveryPartnerRepository;
import com.quickport.deliveryapp.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.Buffer;

@Slf4j
@Service
public class WalletService {
    @Autowired
    DeliveryPartnerRepository deliveryPartnerRepository;

    @Autowired
    WalletRepository walletRepository;

    public Wallet registerWallet(WalletRegister request){
        DeliveryPartner partner = deliveryPartnerRepository.findById(request.getPartnerId())
                .orElseThrow(()-> new RuntimeException("Partner doesn't exists"));

        // If wallet already exists for the partner don't create one
        if(walletRepository.existsByPartnerId(request.getPartnerId()))
            throw new RuntimeException("Wallet already exists");

        //Create a new wallet entity:
        Wallet wallet = Wallet.builder()
                .totalEarning(0D)
                .bank(request.getBankName())
                .ifsc(request.getIfsc())
                .accountNumber(request.getAccNumber())
                .accountHolderName(request.getAccHolderName())
                .partner(partner)
                .build();

        return walletRepository.save(wallet);
    }
}
