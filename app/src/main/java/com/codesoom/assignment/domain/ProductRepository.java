package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * DB에서 상품 조회, 저장, 삭제 작업을 하는 레포지토리
 */
public interface ProductRepository {
    /**
     * 전체 상품 리스트를 반환한다.
     * 
     * @return 전체 상품 리스트
     */
    List<Product> findAll();

    /**
     * id와 일치하는 상품을 반환한다.
     *
     * @param id 상품의 id
     * @return 조회된 상품
     */
    Optional<Product> findById(Long id);

    /**
     * 상품을 저장한 후 저장된 상품을 반환한다.
     * 
     * @param product 저장할 상품
     * @return 저장된 상품
     */
    Product save(Product product);

    /**
     * 상품을 삭제한다.
     * 
     * @param product 삭제할 상품
     */
    void delete(Product product);
}
