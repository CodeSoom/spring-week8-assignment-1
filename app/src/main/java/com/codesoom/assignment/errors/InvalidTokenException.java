package com.codesoom.assignment.errors;

/**
 * 유효하지 않은 토큰을 받았을 때, 던지는 예외.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
