package com.codesoom.assignment.errors;

/**
 * 해당 이메일로 로그인을 실패할 때의 예외.
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
