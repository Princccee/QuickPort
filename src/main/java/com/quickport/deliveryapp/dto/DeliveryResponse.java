package com.quickport.deliveryapp.dto;

import com.quickport.deliveryapp.entity.Address;
import com.quickport.deliveryapp.entity.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryResponse {
    private Long deliveryId;
    private String packageDescription;
    private double fare;
    private String status;
    private LocalDateTime pickupTime;
    private String pickupStreet;
    private String pickupCity;
    private String dropStreet;
    private String dropCity;
    private String customerName;
    private String customerPhone;
}
