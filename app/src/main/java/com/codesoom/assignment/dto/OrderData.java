package com.codesoom.assignment.dto;

/**
 * 주문 정보를 담고 있습니다.
 */
public class OrderData {
    private final Long productId;
    private final int quantity;

    public OrderData(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
