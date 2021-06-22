package com.codesoom.assignment.errors;

/**
 * 잘못된 접근 예외.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
