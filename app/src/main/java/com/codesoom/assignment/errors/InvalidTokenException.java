package com.codesoom.assignment.errors;

/**
 * 인증 토큰이 유효하지 않을 때의 예외.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
