package com.codesoom.assignment.errors;

/**
 * 회원을 찾지 못했음을 나타내는 예외.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * 식별자로 회원을 찾지 못할 경우 던집니다.
     *
     * @param id 식별자
     */
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
