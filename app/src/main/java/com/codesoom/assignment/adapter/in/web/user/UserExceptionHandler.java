package com.codesoom.assignment.adapter.in.web.user;

import com.codesoom.assignment.common.exception.dto.ErrorResponse;
import com.codesoom.assignment.user.application.exception.UserEmailDuplicationException;
import com.codesoom.assignment.user.application.exception.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * User 도메인 관련 예외를 핸들링합니다.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class UserExceptionHandler {

    /**
     * 회원을 찾지 못했을 때 던지는 예외를 핸들링합니다.
     *
     * @param exception 회원을 찾지 못했을 경우
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public static ErrorResponse handleUserNotFound(final UserNotFoundException exception) {
        return ErrorResponse.from(exception);
    }

    /**
     * 회원 이메일이 중복될 때 던지는 예외를 핸들링합니다.
     *
     * @param exception 회원 이메일이 중복일 경우
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public static ErrorResponse handleUserEmailIsAlreadyExisted(final UserEmailDuplicationException exception) {
        return ErrorResponse.from(exception);
    }
}
