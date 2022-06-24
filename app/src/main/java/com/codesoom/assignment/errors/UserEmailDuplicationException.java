package com.codesoom.assignment.errors;

/**
 * 회원 이메일이 이미 존재할 때 발생하는 예외
 */
public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
