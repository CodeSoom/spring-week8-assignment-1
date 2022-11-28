package com.codesoom.assignment.session.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
