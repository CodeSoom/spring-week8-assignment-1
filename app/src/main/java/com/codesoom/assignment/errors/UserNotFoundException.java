package com.codesoom.assignment.errors;

/**
 * 사용자가 존재하지않은 경우 던집니다.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
