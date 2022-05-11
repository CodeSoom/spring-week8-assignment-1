package com.codesoom.assignment.exceptions;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String email) {
        super("[" + email + "]이메일 또는 비밀번호를 다시 확인해주세요");
    }
}
