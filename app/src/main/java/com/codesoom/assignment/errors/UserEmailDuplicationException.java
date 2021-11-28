package com.codesoom.assignment.errors;

/**
 * 사용자 이메일이 중복일 경우 발생시킬 예외 클래스
 */
public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
