package com.codesoom.assignment.exceptions;

import org.springframework.http.HttpStatus;

/** 인증 토큰 유효성 검증 실패 시 던집니다. */
public class InvalidTokenException extends RuntimeException {

    private String code;

    public InvalidTokenException(String message){
        super(message);
        this.code = String.valueOf(HttpStatus.UNAUTHORIZED.value());
    }

    public String getCode() {
        return code;
    }

}
