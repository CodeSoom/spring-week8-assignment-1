package com.codesoom.assignment.errors;

/**
 * 사용자를 찾지 못할 경우 발생되는 예외 클래스
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
