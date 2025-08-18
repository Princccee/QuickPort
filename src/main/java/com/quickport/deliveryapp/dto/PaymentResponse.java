package com.quickport.deliveryapp.dto;

import com.quickport.deliveryapp.entity.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long deliveryRequestId;
    private Long partnerId;
    private Double amount;
    private LocalDateTime paymentTime;
    private PaymentStatus status;
}
