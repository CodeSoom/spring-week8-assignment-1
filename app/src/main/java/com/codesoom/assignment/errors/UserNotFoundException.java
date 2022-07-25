package com.codesoom.assignment.errors;

/**
 * 회원을 찾지 못했을 때 발생하는 예외
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
