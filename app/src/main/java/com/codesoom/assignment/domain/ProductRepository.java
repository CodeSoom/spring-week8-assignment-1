package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품 저장소
 */
public interface ProductRepository {
    /**
     * 상품 리스트를 조회한다.
     * @return
     */
    List<Product> findAll();

    /**
     * 상품을 조회한다.
     * @param id 상품 아이디.
     * @return 상품
     */
    Optional<Product> findById(Long id);

    /**
     * 상품을 저장한다.
     * @param product
     * @return 상품
     */
    Product save(Product product);

    /**
     * 상품을 삭제한다.
     * @param product
     */
    void delete(Product product);
}
