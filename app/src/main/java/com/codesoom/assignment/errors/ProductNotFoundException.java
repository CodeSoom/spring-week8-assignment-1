package com.codesoom.assignment.errors;

/**
 * 상품을 찾지 못했을 때 발생하는 예외
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
