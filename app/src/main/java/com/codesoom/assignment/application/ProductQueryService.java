package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;

public interface ProductQueryService {
    Product findById(Long id);
}
