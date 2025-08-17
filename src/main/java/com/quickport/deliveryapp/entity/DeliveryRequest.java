package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String packageDescription;

    private Double packageWeight;

    private Double fare;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime pickupTime;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "pickup_address_id")
    private Address pickupAddress;

    @ManyToOne
    @JoinColumn(name = "drop_address_id")
    private Address dropAddress;

    @ManyToOne
    @JoinColumn(name = "assigned_partner_id")
    private DeliveryPartner deliveryPartner;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = DeliveryStatus.PENDING;
    }
}
