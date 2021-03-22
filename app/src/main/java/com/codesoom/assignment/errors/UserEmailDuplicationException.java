package com.codesoom.assignment.errors;

/**
 * 회원가입 시 주어진 사용자 이메일이 이미 존재하는 이메일일 경우 발생하는 예외.
 */
public class UserEmailDuplicationException extends RuntimeException{
    public UserEmailDuplicationException(String email) {
        super("User email is already existed: " + email);
    }
}
