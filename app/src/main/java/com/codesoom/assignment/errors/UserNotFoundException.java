package com.codesoom.assignment.errors;

/**
 * 사용자를 찾지 못할경우 발생시킬 예외 클래스
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
