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
 * 상품 조회, 생성, 수정, 삭제 서비스
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
     *  id로 조회된 상품을 반환한다.
     *  
     * @param id 상품의 id
     * @return 조회된 상품
     * @throws ProductNotFoundException id와 일치하는 상품이 조회되지 않은 경우에 발생
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 생성하고, 생성된 상품을 반환한다.
     *
     * @param productData 생성할 상품의 정보
     * @return 생성된 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * id로 조회된 상품의 정보를 수정하고, 수정된 상품을 반환한다.
     *
     * @param id 상품의 id
     * @param productData 수정할 정보
     * @return 수정된 상품
     * @throws ProductNotFoundException id와 일치하는 상품이 조회되지 않은 경우에 발생
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * id로 조회된 상품을 삭제하고, 삭제된 상품을 반환한다.
     * 
     * @param id 상품의 id
     * @return 삭제된 상품
     * @throws ProductNotFoundException id와 일치하는 상품이 조회되지 않은 경우에 발생
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * id로 상품을 찾고, 찾은 상품을 반환한다.
     * 
     * @param id 상품의 id
     * @return 조회된 상품
     * @throws ProductNotFoundException id와 일치하는 상품이 조회되지 않은 경우에 발생
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
