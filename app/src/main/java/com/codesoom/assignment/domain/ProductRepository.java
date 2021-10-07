package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품 조회, 저장, 삭제를 담당한다.
 */
public interface ProductRepository {
    /**
     * 상품의 목록을 리턴한다.
     *
     * @return 상품의 목록
     */
    List<Product> findAll();

    /**
     * id에 해당하는 상품을 리턴한다.
     *
     * @param id 조회할 상품의 id
     * @return 조회 결과
     */
    Optional<Product> findById(Long id);

    /**
     * 상품을 저장한다.
     *
     * @param product 저장할 상품
     * @return 저장한 상품
     */
    Product save(Product product);

    /**
     * 상품을 삭제한다.
     *
     * @param product 삭제할 상품
     */
    void delete(Product product);
}
