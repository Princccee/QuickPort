package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String registrationNumber;

    private Double maxWeight;
    private String model;

    @OneToOne
    @JoinColumn(name = "partner_id")
    private DeliveryPartner partner;

}
