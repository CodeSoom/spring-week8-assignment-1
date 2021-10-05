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
 * 상품 조회, 수정, 삭제 기능을 제공하는 서비스
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
     * 모든 상품 목록을 리턴합니다.
     * @return 상품 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품 하나를 조회해 리턴합니다.
     * @param id 조회할 상품의 id
     * @return 조회한 상품
     * @throws ProductNotFoundException 상품을 찾지 못할 경우
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 저장소에 등록합니다.
     * @param productData 등록할 상품의 내용
     * @return 등록한 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품의 내용을 수정합니다.
     * @param id 수정할 상품의 id
     * @param productData 수정할 상품의 내용
     * @return 수정한 상품
     * @throws ProductNotFoundException 수정할 상품을 못찾을 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품을 삭제합니다.
     * @param id 삭제할 상품의 id
     * @return 삭제한 상품
     * @throws ProductNotFoundException 삭제할 상품을 찾지 못할 경우
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 상품을 찾아 리턴합니다.
     * @param id 찾을 상품의 id
     * @return 찾은 상품
     * @throws ProductNotFoundException 상품을 찾지 못할 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
