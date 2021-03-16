package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품 저장소.
 */
public interface ProductRepository {
    /**
     * 저장소에 존재하는 모든 상품 목록을 반환합니다.
     *
     * @return 상품 목록
     */
    List<Product> findAll();

    /**
     * 대응되는 식별자의 상품을 반환합니다.
     *
     * @param id 상품 식별자
     * @return 대응되는 식별자의 상품
     */
    Optional<Product> findById(Long id);

    /**
     * 주어진 상품정보로 상품을 저장하고 반환합니다.
     *
     * @param product 상품 정보
     * @return 저장된 상품
     */
    Product save(Product product);

    /**
     * 주어진 상품을 삭제합니다.
     *
     * @param product 삭제할 상품
     */
    void delete(Product product);
}
