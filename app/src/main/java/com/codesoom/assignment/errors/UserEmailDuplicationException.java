package com.codesoom.assignment.errors;

/**
 * User email 이 중복되는 경우 예외 처리
 */
public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
