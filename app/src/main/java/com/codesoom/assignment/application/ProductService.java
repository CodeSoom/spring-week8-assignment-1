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
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id에 해당하는 상품을 리턴합니다.
     *
     * @param id 상품의 식별자
     * @return 주어진 id에 해당하는 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    public Product getProduct(Long id) throws ProductNotFoundException {
        return findProduct(id);
    }

    /**
     * 주어진 상품 정보로 상품을 생성한 뒤, 생성된 상품을 리턴합니다.
     *
     * @param productData 상품 정보
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 id에 해당하는 상품을 전달받은 상품 정보로 수정한 뒤, 수정된 상품을 리턴합니다.
     *
     * @param id 상품의 식별자
     * @param productData 상품 정보
     * @return 수정된 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    public Product updateProduct(Long id, ProductData productData)
            throws ProductNotFoundException {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 id에 해당하는 상품을 삭제합니다.
     *
     * @param id 상품의 식별자
     * @return 삭제된 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    public Product deleteProduct(Long id) throws ProductNotFoundException {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    private Product findProduct(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
