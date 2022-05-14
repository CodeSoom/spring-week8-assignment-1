package com.codesoom.assignment.exceptions;

import org.springframework.http.HttpStatus;

/** 회원 로그인 요청 시 비밀번호가 불일치 할 경우 던집니다. */
public class InvalidPasswordException extends RuntimeException {

    private String code;

    public InvalidPasswordException(String message, String code) {
        super(message);
        this.code = code;
    }

    public InvalidPasswordException(String message) {
        this(message, String.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    public String getCode() {
        return code;
    }
}
