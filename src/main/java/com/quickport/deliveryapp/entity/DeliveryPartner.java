package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_partners")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String licenceNumber;

    private String aadhaarNumber;

    private boolean isVerified;

    private String profilePhotoUrl;

    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL)
    private Vehicle vehicle;

    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL)
    private PartnerLocation location;
}
