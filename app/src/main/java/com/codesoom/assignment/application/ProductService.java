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
 * 상품 관련 비즈니스 로직을 담당한다.
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
     * 모든 상품을 반환한다.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id에 해당하는 상품을 반환한다.
     *
     * @param id 상품의 식별자
     * @return 주어진 id에 해당하는 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    public Product getProduct(Long id) throws ProductNotFoundException {
        return findProduct(id);
    }

    /**
     * 주어진 상품 데이터로 상품을 생성하고 상품을 반환한다.
     *
     * @param productData 상품 정보
     * @return 생성한 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 id에 해당하는 상품을 주어진 상품 데이터로 수정하고 수정한 상품을 반환한다.
     *
     * @param id 수정할 상품의 식별자
     * @param productData 수정할 상품 데이터
     * @return 수정한 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    public Product updateProduct(Long id, ProductData productData)
            throws ProductNotFoundException {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 id에 해당하는 상품을 삭제한다.
     *
     * @param id 삭제할 상품의 식별자
     * @return 삭제한 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    public Product deleteProduct(Long id) throws ProductNotFoundException {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
