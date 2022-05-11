package com.codesoom.assignment.domain;

public interface TestProductRepositoryDouble extends ProductRepository {
    @Override
    void delete(Product product);
}
