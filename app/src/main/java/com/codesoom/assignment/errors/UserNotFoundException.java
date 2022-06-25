package com.codesoom.assignment.errors;

/**
 * User 를 찾지 못하는 경우 예외 처리
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
