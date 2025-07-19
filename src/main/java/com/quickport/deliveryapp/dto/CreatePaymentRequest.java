package com.quickport.deliveryapp.dto;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long deliveryRequestId;
    private Double amount;
}
