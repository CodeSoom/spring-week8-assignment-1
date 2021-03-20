package com.codesoom.assignment.errors;

/**
 * 유저를 찾을 수 없을 때, 던져지는 예외.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
