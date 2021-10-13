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
 * 전역 예외 처리 컨트롤러
 */
@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {
    /**
     * 상품 정보를 찾을 수 없는 경우 예외 응답을 반환 한다.
     *
     * @return 예외 응답
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound() {
        return new ErrorResponse("Product not found");
    }

    /**
     * 회원 정보를 찾을 수 없는 경우 예외 응답을 반환 한다.
     *
     * @return 예외 응답
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("User not found");
    }

    /**
     * 회원의 이메일 정보가 중복되는 경우 예외 응답을 반환 한다.
     *
     * @return 예외 응답
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExisted() {
        return new ErrorResponse("User's email address is already existed");
    }

    /**
     * 로그인에 실패한 경우 예외 응답을 반환한다.
     *
     * @return 예외 응답
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException() {
        return new ErrorResponse("Log-in failed");
    }
}
