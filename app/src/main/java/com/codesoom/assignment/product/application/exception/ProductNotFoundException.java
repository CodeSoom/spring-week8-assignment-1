package com.codesoom.assignment.product.application.exception;

public class ProductNotFoundException extends RuntimeException {
    private static final String MESSAGE = "상품이 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
