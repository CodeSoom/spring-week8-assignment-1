package com.codesoom.assignment.domain;

import java.util.List;

public interface ProductRepository {
    Product save(Product product);
    void deleteAll();
    List<Product> findAll();
}
