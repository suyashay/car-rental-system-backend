package com.carrental.backend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

public class ErrorResponse {

    @Getter
    private String message;

    @Getter
    private LocalDateTime timestamp;

    @Getter
    private int  status;

    public ErrorResponse(String message, int status) {

        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

}
