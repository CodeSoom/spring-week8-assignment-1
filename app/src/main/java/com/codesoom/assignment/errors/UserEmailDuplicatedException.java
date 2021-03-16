package com.codesoom.assignment.errors;

/** 중복된 사용자 이메일인 경우에 발생한다. */
public class UserEmailDuplicatedException extends RuntimeException {
    public UserEmailDuplicatedException(String email) {
        super("User email is already existed: " + email);
    }
}
