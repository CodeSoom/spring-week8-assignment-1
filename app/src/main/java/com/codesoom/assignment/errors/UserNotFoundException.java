package com.codesoom.assignment.errors;

/**
 * 유저를 찾을 수 없는 예외.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
