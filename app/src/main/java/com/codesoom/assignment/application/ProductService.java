package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 상품 관련 로직을 처리합니다.
 */
@Service
@Transactional
public class ProductService {
    private final Mapper mapper;
    private final ProductRepository productRepository;

    public ProductService(
            Mapper dozerMapper,
            ProductRepository productRepository
    ) {
        this.mapper = dozerMapper;
        this.productRepository = productRepository;
    }

    /**
     * 등록된 모든 상품 목록을 반환합니다.
     *
     * @return 등록된 상품 리스트
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * id에 해당하는 상품을 반환합니다.
     *
     * @param id 조회할 상품의 식별자
     * @return 조회한 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 저장하고 저장한 상품을 반환합니다.
     *
     * @param productData 저장할 상품의 정보
     * @return 저장한 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * id에 해당하는 상품의 정보를 수정하고 수정된 상품을 반환합니다.
     *
     * @param id          수정할 상품의 식별자
     * @param productData 수정할 상품 정보
     * @return 수정된 상품
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * id에 해당하는 상품을 삭제합니다.
     *
     * @param id 삭제할 상품의 식별자
     * @return 삭제한 상품
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * id에 해당하는 상품을 반환합니다.
     *
     * @param id 조회할 상품의 식별자
     * @return 조회한 상품
     * @throws ProductNotFoundException 상품이 없는 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
