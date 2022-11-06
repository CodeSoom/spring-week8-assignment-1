package com.codesoom.assignment.errors;

/**
 * 이메일을 가진 회원이 이미 존재하는 경우 던집니다.
 */
public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
