package com.quickport.deliveryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rating_reviews")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private int rating; // 1 --> 5

    private String review;

    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryRequest delivery;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private DeliveryPartner partner;

    @PrePersist
    public void setTimestamp() {
        this.reviewDate = LocalDateTime.now();
    }

}
