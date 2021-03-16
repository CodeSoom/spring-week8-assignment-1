package com.codesoom.assignment.errors;

/** 잘못된 인증 요청인 경우에 발생한다. */
public class AuthenticationBadRequestException extends RuntimeException{
    public AuthenticationBadRequestException() {
        super("Authentication bad request");
    }
}
