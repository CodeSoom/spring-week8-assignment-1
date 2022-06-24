package com.codesoom.assignment.errors;

/**
 * 로그인 실패 시 발생하는 예외
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
