package com.codesoom.assignment.errors;

/** 주어진 {@code variable}에 잘못된 상품 요청인 경우에 발생한다. */
public class ProductBadRequestException extends RuntimeException{
    public ProductBadRequestException(String variable) {
        super("Product bad request: " + variable);
    }
}
