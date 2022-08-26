package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.Product;
import lombok.Getter;

@Getter
public class ProductInquiryInfo {
    private final Long id;
    private final Long userId;
    private final String name;
    private final String description;
    private final int quantity;
    private final Integer price;

    public ProductInquiryInfo(Long id, Long userId, String name, String description, int quantity, Integer price) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public static ProductInquiryInfoBuilder builder() {
        return new ProductInquiryInfoBuilder();
    }

    public static class ProductInquiryInfoBuilder {
        private Long id;
        private Long userId;
        private String name;
        private String description;
        private int quantity;
        private Integer price;

        ProductInquiryInfoBuilder() {}

        public ProductInquiryInfoBuilder product(final Product product) {
            this.id = product.getId();
            this.userId = product.getOwner().getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.quantity = product.getQuantity();
            this.price = product.getPrice();
            return this;
        }

        public ProductInquiryInfo build() {
            return new ProductInquiryInfo(this.id, this.userId, this.name, this.description, this.quantity, this.price);
        }
    }
}
