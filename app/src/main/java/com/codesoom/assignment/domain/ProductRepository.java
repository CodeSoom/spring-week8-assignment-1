package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품을 찾고, 저장하고, 삭제하는 메소드 입니다.
 */

public interface ProductRepository {
    /**
     * 모든 상품을 조회하는 메소드
     */
    List<Product> findAll();

    /**
     * 주어진 아이디로 상품을 조회하는 메소드
     * @param id 상품의 아이디
     */
    Optional<Product> findById(Long id);

    /**
     * 상품을 저장하는 메소드
     * @param product 상품
     */
    Product save(Product product);

    /**
     * 상품을 삭제하는 메소드
     * @param product 상품
     */
    void delete(Product product);
}
