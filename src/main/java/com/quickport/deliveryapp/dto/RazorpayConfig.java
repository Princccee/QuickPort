package com.quickport.deliveryapp.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "razorpay")
@Data
public class RazorpayConfig {
    private String keyId;
    private String keySecret;
}
