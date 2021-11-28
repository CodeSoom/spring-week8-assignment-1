package com.codesoom.assignment.errors;

/**
 * 토큰이 올바르지 않을 경우 나타내는 예외
 */
public class InvalidTokenException extends RuntimeException {

    /**
     * 토큰이 올바르지 않을 경우 던집니다.
     *
     * @param token 올바르지 않은 토큰
     */
    public InvalidTokenException(String token) {
        super("Invalid token: " + token);
    }
}
