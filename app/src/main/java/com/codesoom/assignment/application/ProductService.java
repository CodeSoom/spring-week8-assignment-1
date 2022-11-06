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
 * 상품을 관리하는 서비스.
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
     * 모든 상품을 조회합니다.
     * @return 모든 상품
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품을 조회합니다.
     * @param id 조회할 상품 아이디
     * @return 조회한 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 생성합니다.
     * @param productData 생성할 상품 정보
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품을 수정합니다.
     * @param id 수정할 상품 아이디
     * @param productData 수정할 상품 정보
     * @return 수정된 상품
     * @throws ProductNotFoundException 수정할 상품을 찾지 못한 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품을 삭제합니다.
     * @param id 삭제할 상품 아이디
     * @return 삭제된 상품
     * @throws ProductNotFoundException 삭제할 상품을 찾지 못한 경우
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 상품을 조회합니다.
     * @param id 조회할 상품 아이디
     * @return 조회한 상품
     * @throws ProductNotFoundException 상품을 찾지 못한 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
