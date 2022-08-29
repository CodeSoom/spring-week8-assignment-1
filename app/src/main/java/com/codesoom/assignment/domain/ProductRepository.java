package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    void deleteAll();

    List<Product> findAll();

    Optional<Product> findById(Long productId);
}
