package com.codesoom.assignment.errors;

/**
 * Product 를 찾지 못하는 경우 예외 처리
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
