package com.codesoom.assignment.errors;

import com.codesoom.assignment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ErrorResponse handleProductNotFound(ProductNotFoundException e) {
        return new ErrorResponse("상품 " + e.getMessage() + " 제품을 찾을 수 없습니다.");
    }
}
