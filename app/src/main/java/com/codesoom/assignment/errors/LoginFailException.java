package com.codesoom.assignment.errors;

/**
 * User 가 로그인에 실패하는 경우 예외 처리
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
