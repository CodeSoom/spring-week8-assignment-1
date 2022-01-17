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
 * 상품을 관리한다.
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
     * 이 어플리케이션에 있는 모든 상품목록을 리턴합니다.
     * @return 모든 상품들
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id에 해당하는 상품을 리턴합니다.
     * @param id 는 상품을 식별하는 고유 값.
     * @return id에 해당하는 상품.
     * @throws ProductNotFoundException 상품을 찾을 수 없을 경우 발생.
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 주어진 상품 정보를 저장하고 리턴합니다.
     * @param productData 주어진 상품 정보
     * @return 상품 정보
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 id에 해당하는 상품의 정보를 수정하고 수정된 정보를 리턴합니다.
     * @param id 상품 식별자.
     * @param productData 수정된 상품 정보.
     * @return 수정된 상품.
     * @throws ProductNotFoundException 상품을 찾을 수 없을 경우 발생.
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 id에 해당하는 상품을 삭제하고 리턴합니다.
     * @param id 상품 식별자.
     * @return 삭제된 상품.
     * @throws ProductNotFoundException 상품을 찾을 수 없을 경우 발생.
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 주어진 id에 해당하는 상품을 찾고 있다면 상품을 리턴하고
     * 없다면 상품을 찾을 수 없다는 예외를 던진다.
     * @param id 상품 식별자.
     * @return 해당 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없을 경우 발생.
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
