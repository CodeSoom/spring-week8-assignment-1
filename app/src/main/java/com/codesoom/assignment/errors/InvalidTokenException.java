package com.codesoom.assignment.errors;

/** 유효하지 않은 토큰인 경우에 발생한다. */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
