package com.quickport.deliveryapp.dto;

import lombok.Data;

@Data
public class DeliveryRequestDTO {
    private Long pickupAddressId;
    private Long dropAddressId;
    private String packageDescription;
    private Double packageWeight;
}
