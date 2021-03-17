package com.codesoom.assignment.errors;

/**
 * 회원을 찾을 수 없는 예외
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
