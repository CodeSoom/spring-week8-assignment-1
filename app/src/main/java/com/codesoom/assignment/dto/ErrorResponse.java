package com.codesoom.assignment.dto;

/** 예외 메세지를 정의한다. */
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    /** 예외 메세지를 리턴한다. */
    public String getMessage() {
        return message;
    }
}
