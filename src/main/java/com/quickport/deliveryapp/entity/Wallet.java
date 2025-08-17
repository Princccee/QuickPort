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

    private Double totalEarning;

    @Column(name = "bank", nullable = false)
    private String bank;

    @Column(name = "ifsc", nullable = false)
    private String ifsc;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    // Each wallet belongs to one delivery partner
    @OneToOne
    @JoinColumn(name = "partner_id", nullable = false, unique = true)
    private DeliveryPartner partner;
}
