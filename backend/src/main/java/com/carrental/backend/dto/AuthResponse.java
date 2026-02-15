package com.carrental.backend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

public class AuthResponse {

    @Getter
    private String message;

    @Getter
    private LocalDateTime timestamp;

    public AuthResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
