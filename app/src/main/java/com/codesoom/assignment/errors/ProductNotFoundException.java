package com.codesoom.assignment.errors;

/**
 * 상품을 찾지 못할경우 발생시킬 예외 클래스
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
