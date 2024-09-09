package org.example.ted.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";
    private long user_id;

    public AuthResponseDto(String accessToken, long userId) {
        this.accessToken = accessToken;
        this.user_id = userId;
    }
}
