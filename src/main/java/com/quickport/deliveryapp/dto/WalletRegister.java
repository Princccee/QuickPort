package com.quickport.deliveryapp.dto;

import lombok.Data;

@Data
public class WalletRegister {
    private String bankName;
    private String ifsc;
    private String accNumber;
    private String accHolderName;
    private Long partnerId;
}
