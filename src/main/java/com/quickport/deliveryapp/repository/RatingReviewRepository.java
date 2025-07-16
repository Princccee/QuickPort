package com.quickport.deliveryapp.repository;

import com.quickport.deliveryapp.entity.RatingReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingReviewRepository extends JpaRepository<RatingReview,Long> {
    List<RatingReview> findByPartnerId(Long partnerId);
    List<RatingReview> findByDeliveryId(Long deliveryId);
}


