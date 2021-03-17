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
 * 상품과 관련된 비즈니스 로직을 담당합니다.
 *
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
     * 상품 목록 반환.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품을 찾아 반환.
     *
     * @param id 검색 상품 id.
     * @return 상품.
     * @throws ProductNotFoundException 상품이 존재하지 않는 경우.
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 생성하고 반환.
     *
     * @param productData 저장될 상품.
     * @return 생성된 상품.
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품을 수정하고 반환.
     *
     * @param id 수정 상품 id
     * @param productData 수정될 상품.
     * @return 수정된 상품.
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품을 삭제하고 반환.
     *
     * @param id 삭제 상품 id.
     * @return 삭제된 상품.
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 상품을 찾아 반환.
     *
     * @param id 검색 상품 id.
     * @return 상품.
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
