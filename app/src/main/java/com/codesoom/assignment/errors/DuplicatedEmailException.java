package com.codesoom.assignment.errors;

/**
 * 중복된 이메일이 이미 있는 경우 던집니다.
 */
public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(String message) {
        super(message + " 중복된 이메일입니다.");
    }
}
