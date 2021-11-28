package com.codesoom.assignment.dto;

/**
 * 요청에 실패했을 경우 쓰이는 에러메시지 클래스
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
