package com.codesoom.assignment.errors;

/**
 * 로그인에 실패할 경우 발생하는 예외.
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
