package com.codesoom.assignment.common;

public class ErrorResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }
}
