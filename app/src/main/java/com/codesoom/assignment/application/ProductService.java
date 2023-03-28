package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    private final Mapper mapper;
    private final ProductRepository productRepository;

    /**
     * ProductService 생성자를 호출한다.
     *
     * @param dozerMapper - Bean Object Mapper
     * @param productRepository - 제품 리포지토리
     */
    public ProductService(
            Mapper dozerMapper,
            ProductRepository productRepository
    ) {
        this.mapper = dozerMapper;
        this.productRepository = productRepository;
    }

    /**
     * 모든 제품 리스트를 조회한다.
     * 
     * @return 제품 리스트.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * ID에 대한 제품을 조회한다.
     *
     * @param id - 제품 고유 식별 ID.
     * @return ID에 대한 제품.
     * @Throws ProductNotFoundException - ID에 대한 제품이 없을 경우 예외 발생.
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 제품을 생성한다.
     *
     * @param productData - 제품 생성 정보 객체.
     * @return 생성된 제품 엔티티.
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 제품을 수정한다.
     *
     * @param id - 수정할 제품 ID.
     * @param productData - 제품 수정 정보 객체.
     * @return 수정된 제품 엔티티.
     * @throws ProductNotFoundException - ID에 대한 제품이 없을 경우 예외 발생.
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 제품을 삭제한다.
     *
     * @param id - 삭제할 제품 ID.
     * @return 삭제된 제품 엔티티.
     * @throws ProductNotFoundException - ID에 대한 제품이 없을 경우 예외 발생.
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * ID에 대한 제품 엔티티 조회
     * @param id - 조회할 제품 ID
     * @return 제품 엔티티
     * @throws ProductNotFoundException - ID에 대한 제품이 없을 경우 예외 발생.
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
