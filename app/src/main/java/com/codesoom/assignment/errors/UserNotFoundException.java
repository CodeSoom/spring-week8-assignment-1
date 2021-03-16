package com.codesoom.assignment.errors;

/** 주어진 {@code id}에 해당하는 사용자가 없는 경우에 발생한다. */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
