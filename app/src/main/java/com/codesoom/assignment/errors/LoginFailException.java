package com.codesoom.assignment.errors;

/**
 * 유저 인증 실패 예외.
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
