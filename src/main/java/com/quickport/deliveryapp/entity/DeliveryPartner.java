package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.availability.AvailabilityState;

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

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", nullable = false, unique = true)
//    private User user;
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String phone;

    private String licenceNumber;

    private String aadhaarNumber;

//    private boolean isVerified;

    private String profilePhotoUrl;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL)
    private Vehicle vehicle;

    // Partner Address:
    private Double latitude;

    private Double longitude;

//    @OneToOne(mappedBy = "partner", cascade = CascadeType.ALL)
//    private PartnerLocation location;

    private String fcmToken;

    public enum AvailabilityStatus{
        AVIALABLE,
        OFFLINE,
        ON_DELIVERY,
    }
}
