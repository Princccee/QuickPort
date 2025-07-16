package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String state;

    private String postalCode;

    private String landmark;

    private Double latitude;

    private Double longitude;

    // Many addresses can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
