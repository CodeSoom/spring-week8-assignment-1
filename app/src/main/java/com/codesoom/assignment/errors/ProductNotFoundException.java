package com.codesoom.assignment.errors;

/** 주어진 {@code id}에 해당하는 상품이 없는 경우에 발생한다. */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
