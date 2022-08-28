package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderInfo {
    private final Long orderId;
    private final Long buyerId;
    private final Long productId;
    private final int purchaseQuantity;

    @Builder
    public OrderInfo(Long orderId, Long buyerId, Long productId, int purchaseQuantity) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.productId = productId;
        this.purchaseQuantity = purchaseQuantity;
    }

    public static OrderInfo from(Order order) {
        return OrderInfo.builder()
                .orderId(order.getId())
                .buyerId(order.getBuyer().getId())
                .productId(order.getProduct().getId())
                .purchaseQuantity(order.getQuantity().getQuantity())
                .build();
    }
}
