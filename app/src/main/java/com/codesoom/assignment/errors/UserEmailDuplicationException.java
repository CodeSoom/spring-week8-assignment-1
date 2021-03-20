package com.codesoom.assignment.errors;

/**
 * 유저의 이메일이 중복될 때, 던져지는 예외.
 */
public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
