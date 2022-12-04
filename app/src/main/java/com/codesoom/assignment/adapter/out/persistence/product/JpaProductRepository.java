package com.codesoom.assignment.adapter.out.persistence.product;

import com.codesoom.assignment.product.repository.Product;
import com.codesoom.assignment.product.repository.ProductRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface JpaProductRepository
        extends ProductRepository, CrudRepository<Product, Long> {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void delete(Product product);
}
