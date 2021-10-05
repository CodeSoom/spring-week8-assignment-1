package com.codesoom.assignment.domain;

import java.util.List;
import java.util.Optional;

/**
 * 상품을 저장소에 등록, 찾기, 삭제 기능을 제공하는 클래스
 */
public interface ProductRepository {

    /**
     * 모든 상품을 조회해 리턴합니다.
     * @return 모든 상품의 목록
     */
    List<Product> findAll();

    /**
     * 저장소에서 상품 하나를 찾아 리턴합니다.
     * @param id 찾을 상품의 id
     * @return 찾은 상품
     */
    Optional<Product> findById(Long id);

    /**
     * 상품을 저장소에 등록합니다.
     * @param product 등록할 상품의 내용
     * @return 등록한 상품
     */
    Product save(Product product);

    /**
     * 상품을 저장소에서 삭제합니다.
     * @param product 삭제할 상품의 내용
     */
    void delete(Product product);

}
