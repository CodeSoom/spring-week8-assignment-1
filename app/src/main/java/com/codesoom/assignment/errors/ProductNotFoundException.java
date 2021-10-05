package com.codesoom.assignment.errors;

/**
 * 상품을 찾지 못했음을 나타내는 예외
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * 식별자로 상품을 찾지 못할경우 던집니다.
     *
     * @param id 식별자
     */
    public ProductNotFoundException(Long id) {
        super("Product not found: " + id);
    }
}
