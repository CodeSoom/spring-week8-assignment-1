package com.codesoom.assignment.errors;

/**
 * 로그인에 실패할 경우를 나타내는 예외
 */
public class LoginFailException extends RuntimeException {

    /**
     * 로그인에 실패할 경우 던집니다.
     *
     * @param email 로그인에 실패한 이메일
     */
    public LoginFailException(String email) {
        super("Login fail - email: " + email);
    }
}
