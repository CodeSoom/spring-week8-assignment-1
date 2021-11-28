package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * 상품 관련 처리 담당.
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
     * 모든 상품을 리턴합니다.
     *
     * @return 모든 상품.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 식별자로 상품을 찾아 리턴합니다.
     *
     * @param id 식별자
     * @return 찾은 상품
     * @throws ProductNotFoundException 식별자로 상품을 찾을 수 없는 경우
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품 정보로 상품을 생성하고 리턴합니다.
     *
     * @param productData 생성할 상품 정보
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 식별자로 상품을 찾아 상품 정보를 수정하고, 리턴합니다.
     *
     * @param id          식별자
     * @param productData 수정할 상품 정보
     * @return 수정된 상품
     * @throws ProductNotFoundException 식별자로 상품을 찾을 수 없는 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }


    /**
     * 식별자로 상품을 찾아 삭제하고, 리턴합니다.
     *
     * @param id 식별자
     * @return 삭제된 상품
     * @throws ProductNotFoundException 식별자로 상품을 찾을 수 없는 경우
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 식별자로 상품을 찾아 리턴합니다.
     *
     * @param id 식별자
     * @return 찾은 상품
     * @throws ProductNotFoundException 식별자로 상품을 찾을 수 없는 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
