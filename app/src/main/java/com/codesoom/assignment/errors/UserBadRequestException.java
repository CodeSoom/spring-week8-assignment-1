package com.codesoom.assignment.errors;

/** 잘못된 사용자 요청인 경우에 발생한다. */
public class UserBadRequestException extends RuntimeException {
    public UserBadRequestException() {
        super("User bad request");
    }
}
