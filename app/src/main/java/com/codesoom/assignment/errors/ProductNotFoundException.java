package com.codesoom.assignment.errors;

/**
 * 상품을 찾지 못하면 던집니다.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super(String.valueOf(productId));
    }
}
