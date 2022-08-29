package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductData {
    private final String name;
    private final String description;
    private final int quantity;
    private final Integer price;

    @Builder
    public ProductData(String name, String description, int quantity, Integer price) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public Product toProduct() {
        return Product.builder()
                .name(this.name)
                .description(this.description)
                .quantity(this.quantity)
                .price(this.price)
                .build();
    }
}
