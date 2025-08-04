package com.quickport.deliveryapp.dto;

import com.quickport.deliveryapp.entity.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
//    private Role.RoleType role;
}
