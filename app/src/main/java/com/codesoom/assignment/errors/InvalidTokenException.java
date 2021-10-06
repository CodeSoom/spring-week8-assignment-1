package com.codesoom.assignment.errors;

/**
 * 사용자 토큰이 유효하지 않을 경우 발생시킬 예외 클래스
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
