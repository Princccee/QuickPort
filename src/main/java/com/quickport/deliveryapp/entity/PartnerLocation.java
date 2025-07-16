package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partner_location")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;

    private Double longitude;

    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "partner_id", nullable = false, unique = true)
    private DeliveryPartner partner;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
