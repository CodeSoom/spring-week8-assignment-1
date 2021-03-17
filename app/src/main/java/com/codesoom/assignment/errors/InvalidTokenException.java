package com.codesoom.assignment.errors;

/**
 * 유효하지 않은 토큰 예외.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
