package com.codesoom.assignment.product.domain.port.command;

import com.codesoom.assignment.product.repository.Product;

public interface ProductCreateRequest {
    String getName();

    String getMaker();

    Integer getPrice();

    String getImageUrl();

    Product toEntity();
}
