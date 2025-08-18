package com.quickport.deliveryapp.dto;

import lombok.Data;

@Data
public class PartnerLoginRequest {
    private String email;
    private String password;
}
