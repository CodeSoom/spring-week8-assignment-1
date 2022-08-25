package com.codesoom.assignment.errors;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long productId) {
        super(String.valueOf(productId));
    }
}
