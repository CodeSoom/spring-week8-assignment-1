package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductInquiryInfo {
    private final Long id;
    private final Long userId;
    private final String name;
    private final String description;
    private final int quantity;
    private final Integer price;

    @Builder
    public ProductInquiryInfo(Long id, Long userId, String name, String description, int quantity, Integer price) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public static ProductInquiryInfo from(Product product) {
        return ProductInquiryInfo.builder()
                .id(product.getId())
                .userId(product.getOwner().getId())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .build();
    }
}