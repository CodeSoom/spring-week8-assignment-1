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
 * 상품 데이터의 조회와 변경 작업을 담당합니다.
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
     * 저장된 모든 상품을 리턴합니다.
     * @return 저장된 모든 상품
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 전달된 id에 해당하는 상품을 리턴합니다.
     * @param id 상품 id
     * @return id에 해당하는 상품
     * @throws ProductNotFoundException id에 해당하는 상품을 찾을 수 없는 경우
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 절달된 상품 정보로 상품을 생성하고, 그 상품을 리턴합니다.
     * @param productData 상품 정
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 전달된 id에 해당하는 상품을 찾고, 전달된 상품 정보를 수정한 후, 수정 된 상품을 리턴합니다.
     * @param id
     * @param productData 수정할 상품 정보
     * @return 수정된 상품
     * @throws ProductNotFoundException 주어진 id에 해당하는 상품을 찾을 수 없는 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 전달된 Id에 해당하는 상품을 삭제합니다.
     * @param id
     * @throws ProductNotFoundException 주어진 id에 해당하는 상품을 찾을 수 없는 경우
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 전달된 id에 해당하는 상품을 찾습니다.
     * @param id
     * @throws ProductNotFoundException 상품을 찾을 수 없을 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
