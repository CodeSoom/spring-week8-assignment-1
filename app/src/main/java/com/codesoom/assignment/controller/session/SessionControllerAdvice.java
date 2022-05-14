package com.codesoom.assignment.controller.session;

import com.codesoom.assignment.exceptions.InvalidPasswordException;
import com.codesoom.assignment.exceptions.UserNotFoundException;
import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 로그인 요청에서 발생하는 예외를 처리합니다. */
@RestControllerAdvice(basePackages = "com.codesoom.assignment.controller.session")
public class SessionControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return ErrorResponse.of(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public ErrorResponse handleInvalidPasswordException(InvalidPasswordException e) {
        return ErrorResponse.of(e.getCode(), e.getMessage());
    }

}
