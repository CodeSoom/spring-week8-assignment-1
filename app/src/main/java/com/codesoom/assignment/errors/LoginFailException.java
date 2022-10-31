package com.codesoom.assignment.errors;

/**
 * 이메일을 찾지 못했거나 비밀번호가 일치하지 않는 경우 던집니다.
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
