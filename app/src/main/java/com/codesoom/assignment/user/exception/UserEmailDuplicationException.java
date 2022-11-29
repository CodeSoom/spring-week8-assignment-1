package com.codesoom.assignment.user.exception;

public class UserEmailDuplicationException extends RuntimeException {
    private static final String MESSAGE = "회원의 아이디가 중복됩니다.";

    public UserEmailDuplicationException() {
        super(MESSAGE);
    }
}
