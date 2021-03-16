package com.codesoom.assignment.errors;

/**
 * 유저 not found 예외.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
