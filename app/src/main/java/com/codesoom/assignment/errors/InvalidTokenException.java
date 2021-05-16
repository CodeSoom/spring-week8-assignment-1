package com.codesoom.assignment.errors;

/**
 * 토큰이 유효하지 않을 경우 발생하는 예외.
 */
public class InvalidTokenException extends RuntimeException{
        public InvalidTokenException(String token) {
            super("Invalid token: " + token);
        }
}
