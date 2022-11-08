package com.codesoom.assignment.errors;

/**
 * 제품이 존재하지 않을 경우 던집니다.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
