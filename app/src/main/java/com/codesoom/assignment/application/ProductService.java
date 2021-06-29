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
 * Product에 대한 CRUD 처리를 담당합니다.
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
     * Product 목록을 반환합니다
     *
     * @return Product 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 회원 고유 식별자로 Product를 찾아 반환합니다.
     *
     * @param id Product 식별자
     * @return 검색된 Product
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * Product를 생성합니다.
     *
     * @param productData 생성할 Product 정보
     * @return 생성된 Product
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * Product 정보를 변경합니다.
     *
     * @param id 변경할 Product 식별자
     * @param productData 변경할 Product 정보
     * @return 변경된 Product 정보
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * Product를 삭제합니다.
     *
     * @param id 삭제할 Product 식별자
     * @return 삭제된 Product 정보
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 식별자를 통한 Product를 검색합니다.
     *
     * @param id 검색할 Product 식별자
     * @return 검색된 Product
     * @throws ProductNotFoundException 검색 조건에 맞는 Product가 없을 시 ProductNotFoundException을 던집니다.
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
