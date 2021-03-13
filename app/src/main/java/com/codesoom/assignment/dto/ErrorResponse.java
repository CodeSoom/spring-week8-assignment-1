package com.codesoom.assignment.dto;

/**
 * 에러 발생 메세지 DTO
 */
public class ErrorResponse {
    /**
     * 에러 메세지
     */
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * 에러 메세지를 리턴합니다.
     *
     * @return 에러 메세지
     */
    public String getMessage() {
        return message;
    }
}
