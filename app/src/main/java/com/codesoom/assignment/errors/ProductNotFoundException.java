package com.codesoom.assignment.errors;

/**
 * 상품이 존재하지 않을 때 던져지는 예외.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
