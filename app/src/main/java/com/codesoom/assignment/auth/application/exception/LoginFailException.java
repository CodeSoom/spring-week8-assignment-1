package com.codesoom.assignment.auth.application.exception;

/**
 * 로그인이 실패할 때 던지는 예외입니다. <br>
 * 기본적으로 RuntimeException을 상속받기에, 예외 발생 시 roll-back을 수행합니다.
 */
public class LoginFailException extends RuntimeException {
    private static final String MESSAGE = "회원이 존재하지 않습니다.";

    public LoginFailException() {
        super(MESSAGE);
    }
}
