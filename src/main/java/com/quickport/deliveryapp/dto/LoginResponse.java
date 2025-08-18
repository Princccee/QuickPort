package com.quickport.deliveryapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String message;
    private String jwtToken;
}
