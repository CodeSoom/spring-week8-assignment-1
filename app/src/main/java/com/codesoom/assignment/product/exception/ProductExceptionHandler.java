package com.codesoom.assignment.product.exception;

import com.codesoom.assignment.common.exception.dto.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Product 도메인 관련 예외를 핸들링합니다.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProductExceptionHandler {

    /**
     * 상품을 찾지 못했을 때 던지는 예외를 핸들링합니다.
     *
     * @param exception 상품을 찾지 못했을 때 던지는 예외
     * @return 예외 메세지와 404 응답 코드로 리턴
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(ProductNotFoundException exception) {
        return ErrorResponse.from(exception);
    }
}
