package com.codesoom.assignment.errors;

/**
 * 로그인이 실패했을 때, 던져지는 예외.
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
