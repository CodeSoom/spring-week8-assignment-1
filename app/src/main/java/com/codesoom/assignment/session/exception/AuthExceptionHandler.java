package com.codesoom.assignment.session.exception;

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
public class AuthExceptionHandler {

    /**
     * 로그인이 실패할 때 던지는 예외를 핸들링합니다.
     *
     * @param exception 로그인이 실패할 때 던지는 예외
     * @return 예외 메세지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailException.class)
    public ErrorResponse handleLoginFailException(final LoginFailException exception) {
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
    public ErrorResponse handleInvalidAccessTokenException(final InvalidTokenException exception) {
        return ErrorResponse.from(exception);
    }



    /* TODO: AccessDeniedException을 직접 핸들링하거나, 예외 문구를 바꾸는 법 서칭
       현재는 Spring Security가 알아서 아래와 같이 응답 코드를 던짐
       - isAuthenticated() : 401
       - hasAnyAuthority() : 403

       예외 응답 body message를 handler로 세팅해주려면 AccessDeniedException의 원인이 뭔지 직접 판별해야 됨
    */
//    @ExceptionHandler(AccessDeniedException.class)
//    public ErrorResponse handleAccessDeniedException() {
//        return new ErrorResponse("Access denied");
//    }
}
