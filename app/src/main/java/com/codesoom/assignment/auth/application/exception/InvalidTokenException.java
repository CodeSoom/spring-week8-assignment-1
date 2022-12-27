package com.codesoom.assignment.auth.application.exception;

/**
 * 토큰이 유효하지 않을 때 던지는 예외입니다 <br>
 * 기본적으로 RuntimeException을 상속받기에, 예외 발생 시 roll-back을 수행합니다.
 */
public class InvalidTokenException extends RuntimeException {
    private static final String MESSAGE = "토큰이 유효하지 않습니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }
}
