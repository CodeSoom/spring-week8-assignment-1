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
 * 상품 관리 기능을 제공합니다.
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
     * 모든 상품 목록을 반환합니다.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 아이디로 상품을 찾아서 상품을 반환합니다.
     *
     * @param id 상품의 아이디
     * @throws ProductNotFoundException 상품 아이디가 존재하지 않을 경우
     */

    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 새로운 상품 데이터를 저장하고, 저장된 상품 정보를 리턴합니다.
     * @param productData 저장하려는 상품 데이터
     */

    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 아이디에 해당하는 상품을 수정하고, 수정된 결과를 리턴합니다.
     *
     * @param id 상품의 아이디
     * @param productData 저장하려는 상품 데이터
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 아이디로 삭제할 상품을 찾아서 삭제한 결과를 리턴합니다.
     *
     * @param id 상품의 아이디
     */

    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 주어진 아이디로 상품을 찾아서 해당 상품을 반환합니다.
     *
     * @param id 상품의 아이디
     * @throws ProductNotFoundException 상품 아이디가 존재하지 않을 경우
     */

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
