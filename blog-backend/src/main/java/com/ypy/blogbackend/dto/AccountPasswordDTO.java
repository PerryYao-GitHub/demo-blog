package com.ypy.blogbackend.dto;

import lombok.Data;

@Data
public class AccountPasswordDTO {
    private String account;
    private String password;
    private String confirmPassword;
    private String newPassword;
}
