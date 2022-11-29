package com.codesoom.assignment.product.adapter.in.web.dto.response;

import com.codesoom.assignment.product.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CreateProductResponseDto {
    @JsonIgnore
    private Product product;

    public CreateProductResponseDto(Product product) {
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
