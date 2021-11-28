package com.codesoom.assignment.errors;

/**
 * 이메일이 중복되었음을 나타내는 예외
 */
public class UserEmailDuplicationException extends RuntimeException {

    /**
     * 이메일이 중복된 경우 던집니다.
     *
     * @param email 중복된 이메일
     */
    public UserEmailDuplicationException(String email) {
        super("User email is alreay existed: " + email);
    }
}
