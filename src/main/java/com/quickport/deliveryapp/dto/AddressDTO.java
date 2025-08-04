package com.quickport.deliveryapp.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Long userId;
    private String street;
    private String landmark;
    private String city;
    private String state;
    private String postalCode;
}
