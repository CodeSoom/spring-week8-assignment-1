package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.product.ProductDto;

public interface ProductCreator {
    Product create(ProductDto productDto);
}
