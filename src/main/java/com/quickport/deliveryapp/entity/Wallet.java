package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance;

    private Double totalEarning;

    // Each wallet will belong to one of the delivery partner
    @OneToOne
    @JoinColumn(name = "partner_id", nullable = false, unique = true)
    private DeliveryPartner partner;
}
