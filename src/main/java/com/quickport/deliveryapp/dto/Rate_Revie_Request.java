package com.quickport.deliveryapp.dto;

import lombok.Data;

@Data
public class Rate_Revie_Request {
    private Long deliveryId;
    private int rate; // make sure to be <= 5
    private String review;
}
