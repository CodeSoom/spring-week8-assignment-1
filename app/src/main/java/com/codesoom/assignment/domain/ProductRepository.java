package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 *  Product 레포지토리
 */
public interface ProductRepository {

    /**
     * Products 목록을 반환
     *
     * @return Products 목록
     */
    List<Product> findAll();

    /**
     * Product 정보 유무 객체를 반환
     *
     * @param id Product 고유 식별자
     * @return Product 정보 유무 객체
     */
    Optional<Product> findById(Long id);

    /**
     * Product 정보를 저장
     *
     * @param product 저장 할 Product 정보
     * @return 저장된 Product 정보
     */
    Product save(Product product);

    /**
     * Product의 정보를 삭제
     *
     * @param product 삭제할 Product 정보
     */
    void delete(Product product);
}
