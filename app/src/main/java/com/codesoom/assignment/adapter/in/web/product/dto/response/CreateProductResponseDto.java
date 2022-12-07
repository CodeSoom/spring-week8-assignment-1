package com.codesoom.assignment.adapter.in.web.product.dto.response;

import com.codesoom.assignment.product.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CreateProductResponseDto {
    @JsonIgnore
    private final Product product;

    public CreateProductResponseDto(final Product product) {
        this.product = product;
    }

    public Long getId() {
        return this.product.getId();
    }

    public String getName() {
        return this.product.getName();
    }

    public String getMaker() {
        return this.product.getMaker();
    }

    public Integer getPrice() {
        return this.product.getPrice();
    }

    public String getImageUrl() {
        return this.product.getImageUrl();
    }
}
