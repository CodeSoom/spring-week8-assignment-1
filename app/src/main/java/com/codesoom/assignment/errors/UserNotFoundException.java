package com.codesoom.assignment.errors;

/**
 * 회원을 찾지 못한 경우 던집니다.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
