package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품을 저장소에 찾기,저장,삭제 기능을 제공한다.
 */
public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

    void delete(Product product);
}
