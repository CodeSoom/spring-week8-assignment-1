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
 * HTTP 요청에 따른 예외처리 응답을 제공하는 클래스
 */
@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {

    /**
     * 상품을 찾지 못하는 예외가 발생할 경우, 404 상태코드를 반환합니다.
     * @return 상품을 찾지 못하는 응답 메시지
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Product not found");
    }

    /**
     * 사용자를 찾지 못하는 예외가 발생할 경우, 404 상태코드를 반환합니다.
     * @return 사용자를 찾지 못하는 응답 메시지
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

    /**
     * 사용자 이메일이 중복되어 예외가 발생할 경우, 400 상태코드를 반환합니다.
     * @return 입력한 이메일이 이미 존재한다는 응답 메시지
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("User's email address is already existed");
    }

    /**
     * 로그인에 실패하여 예외가 발생할 경우, 400 상태코드를 반환합니다.
     * @return 로그인 실패 응답 메시지
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException() {
        return new ErrorResponse("Log-in failed");
    }
}
