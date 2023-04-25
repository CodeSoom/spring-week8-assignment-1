package com.codesoom.assignment.controllers;

import com.codesoom.assignment.dto.ErrorResponse;
import com.codesoom.assignment.errors.LoginFailException;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.codesoom.assignment.errors.UserEmailDuplicationException;
import com.codesoom.assignment.errors.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception Handler for Controller
 *
 * @author sim
 */
@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {

    /**
     * ProductNotFoundException 에 대한 핸들러 메서드
     *
     * @return 예외 응답 객체 및 404 에러
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Product not found");
    }

    /**
     * UserNotFoundException 에 대한 핸들러 메서드
     *
     * @return 예외 응답 객체 및 404 에러
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

    /**
     * UserEmailDuplicationException 에 대한 핸들러 메서드
     *
     * @return 예외 응답 객체 및 400 에러
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("User's email address is already existed");
    }

    /**
     * LoginFailException 에 대한 핸들러 메서드
     *
     * @return 예외 응답 객체 및 400 에러
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException() {
        return new ErrorResponse("Log-in failed");
    }
}
