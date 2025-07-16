package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="payments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentReferenceId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentTime;

    // Multiple payments can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User paidBy;

    // One payment will correspond to one delivery
    @OneToOne
    @JoinColumn(name = "delivery_request_id")
    private DeliveryRequest delivery;

    @PrePersist
    public void onCreate() {
        this.paymentTime = LocalDateTime.now();
    }

}
