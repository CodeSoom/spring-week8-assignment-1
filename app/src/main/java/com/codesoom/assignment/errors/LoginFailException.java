package com.codesoom.assignment.errors;

/**
 * 로그인 처리에 실패할 경우 던집니다.
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
