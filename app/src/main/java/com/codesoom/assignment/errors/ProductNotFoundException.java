package com.codesoom.assignment.errors;

/**
 * 상품을 찾지 못한 경우 던집니다.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
