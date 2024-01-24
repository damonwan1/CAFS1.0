package com.as.demo.entity.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String accessToken;
    private String expireTime;
    private String generateTime;
}
