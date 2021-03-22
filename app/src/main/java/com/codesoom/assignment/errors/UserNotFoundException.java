package com.codesoom.assignment.errors;

/**
 * 사용자를 찾을 수 없을 경우 발생하는 예외.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User ID not found: " + id);
    }
}
