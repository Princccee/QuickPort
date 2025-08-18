package com.quickport.deliveryapp.dto;

import com.quickport.deliveryapp.entity.Vehicle;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerRegResponse {
    private String name;
    private String email;
    private String phone;
    private String licenceNumber;
    private String aadharNumber;
    private String vehicleRegNumber;
}
