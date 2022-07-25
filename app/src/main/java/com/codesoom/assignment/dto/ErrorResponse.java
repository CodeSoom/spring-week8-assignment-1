package com.codesoom.assignment.dto;

/**
 * Error 메시지 DTO
 */
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
