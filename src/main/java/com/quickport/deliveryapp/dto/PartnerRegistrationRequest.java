package com.quickport.deliveryapp.dto;

import lombok.Data;

@Data
public class PartnerRegistrationRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String vehicleType;
    private String vehicleRegNumber;
    private Double maxWeight;
    private String model;
    private String licenseNumber;
    private String aadharNumber;
}
