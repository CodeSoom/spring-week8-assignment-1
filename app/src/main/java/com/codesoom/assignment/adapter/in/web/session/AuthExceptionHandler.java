package com.codesoom.assignment.adapter.in.web.session;

import com.codesoom.assignment.auth.application.exception.InvalidTokenException;
import com.codesoom.assignment.auth.application.exception.LoginFailException;
import com.codesoom.assignment.common.exception.dto.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 인증 관련 예외를 핸들링합니다.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class AuthExceptionHandler {

    /**
     * 로그인이 실패할 때 던지는 예외를 핸들링합니다.
     *
     * @param exception 로그인이 실패할 때 던지는 예외
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public static ErrorResponse handleLoginFailException(final LoginFailException exception) {
        return ErrorResponse.from(exception);
    }

    /**
     * 토큰이 유효하지 않을 때 던지는 예외를 핸들링합니다.
     *
     * @param exception 토큰이 유효하지 않을 때 던지는 예외
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public static ErrorResponse handleInvalidAccessTokenException(final InvalidTokenException exception) {
        return ErrorResponse.from(exception);
    }
}
