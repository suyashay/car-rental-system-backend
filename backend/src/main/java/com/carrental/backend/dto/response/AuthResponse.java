package com.carrental.backend.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

public class AuthResponse {

    @Getter
    private String token;

    @Getter
    private LocalDateTime timestamp;

    public AuthResponse(String token) {
        this.token = token;
        this.timestamp = LocalDateTime.now();
    }
}
