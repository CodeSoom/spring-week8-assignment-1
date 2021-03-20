package com.codesoom.assignment.dto;

/**
 * 에러를 전달하는 형식.
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
