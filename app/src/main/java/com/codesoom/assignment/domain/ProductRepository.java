package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품 저장소.
 */
public interface ProductRepository {

    /**
     * 저장된 상품 리스트를 반환한다.
     *
     * @return 저장된 상품 리스트
     */
    List<Product> findAll();

    /**
     * 식별자에 해당하는 상품을 반환한다.
     *
     * @param id 상품 식별자
     * @return 식별자 해당 상품
     */
    Optional<Product> findById(Long id);

    /**
     * 상품 정보로 상품을 저장 후 반환한다.
     *
     * @param product 상품 정보
     * @return 저장된 상품
     */
    Product save(Product product);

    /**
     * 주어진 상품을 삭제한다.
     *
     * @param product 삭제할 상품
     */
    void delete(Product product);
}
