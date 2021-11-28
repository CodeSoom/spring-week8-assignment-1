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
 * 상품과 관련된 비즈니스 로직을 제공합니다.
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
     * 상품 전체 리스트를 반환한다.
     *
     * @return 상품 전체 리스트
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 해당 식별자 상품을 반환한다.
     *
     * @param id 상품 식별자
     * @return 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품 정보를 받아 상품을 생성하고 반환한다.
     *
     * @param productData 상품 정보
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품정보와 식별자로, 해당 상품을 수정한후 반환한다.
     *
     * @param id 상품 식별자
     * @param productData 상품정보
     * @return 수정된 상품
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 식별자에 해당하는 상품을 삭제하고 반환한다.
     *
     * @param id 상품 식별자
     * @return 삭제될 상품
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 식별자에 해당하는 상품을 반환한다.
     *
     * @param id 상품식별자
     * @return 상품
     * @throws ProductNotFoundException 식별자에 해당하는 상품을 찾지 못한 경우
     * */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
