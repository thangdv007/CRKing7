package com.crking7.datn.web.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserResponse {
    private long id;

    private String username;

    private String password;

    private String email;

    private String otp;

    private String firstName;

    private String lastName;

    private String phone;

    private String image;

    private Set<RoleResponse> roles;

    private int status;

    private List<AddressResponse> addresses;

}
