package com.crking7.datn.web.dto.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String password;

    private String cfPassword;
}
