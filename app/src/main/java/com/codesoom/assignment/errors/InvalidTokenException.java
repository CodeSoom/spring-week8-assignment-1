package com.codesoom.assignment.errors;

/**
 * User token 이 유효하지 않는 경우 예외 처리
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
