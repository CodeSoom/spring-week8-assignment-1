package com.codesoom.assignment.errors;

/**
 * 사용자가 로그인에 실패할 경우 발생되는 예외 클래스
 */
public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
