package com.quickport.deliveryapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerLoginResponse {
    private String message;
    private String token;
}
