package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository For Product
 *
 * @author sim
 */
public interface JpaProductRepository
        extends ProductRepository, CrudRepository<Product, Long> {
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
     * @return 제품 정보
     */
    Optional<Product> findById(Long id);

    /**
     * 제품을 저장한다.
     *
     * @param product - 제품 정보 객체
     * @return 저장된 제품 정보
     */
    Product save(Product product);

    /**
     * 제품을 삭제한다.
     *
     * @param product - 삭제할 제품 엔티티
     */
    void delete(Product product);
}
