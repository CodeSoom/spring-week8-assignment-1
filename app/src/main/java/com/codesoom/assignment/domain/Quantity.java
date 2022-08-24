package com.codesoom.assignment.domain;

/**
 * 상품의 수량을 가지고 있습니다.
 */
public class Quantity {
    private final int quantity;

    public Quantity(int quantity) {
        validQuantity(quantity);
        this.quantity = quantity;
    }

    private void validQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 음수일 수 없습니다.");
        }
    }
}
