package com.codesoom.assignment.application.product;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.product.ProductDto;

public interface ProductChanger {
    void deleteById(Long id);
    Product save(Long id, ProductDto productDto);
}
