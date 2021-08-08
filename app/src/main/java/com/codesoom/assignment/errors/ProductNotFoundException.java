package com.codesoom.assignment.errors;

/**
 * 상품을 찾을 수 없는 예외.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
