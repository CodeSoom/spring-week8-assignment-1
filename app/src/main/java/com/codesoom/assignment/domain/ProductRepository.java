package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    /**
     * 모든 제품 리스트를 조회한다.
     * 
     * @return 모든 제품 리스트
     */
    List<Product> findAll();

    /**
     * ID에 대한 제품을 조회한다.
     * 
     * @param id - 제품 고유 식별 ID
     * @return 제품에 대한 Optional 객체
     */
    Optional<Product> findById(Long id);

    /**
     * 제품을 저장한다.
     * 
     * @param product - 제품 정보 객체
     * @return 저장된 제품 엔티티
     */
    Product save(Product product);

    /**
     * 제품을 삭제한다.
     * 
     * @param product - 삭제할 제품 엔티티
     */
    void delete(Product product);
}
