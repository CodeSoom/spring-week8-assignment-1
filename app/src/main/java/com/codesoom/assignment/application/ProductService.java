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
 * Product 서비스 클래스
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
     * 모든 Product를 반환한다.
     * @return 모든 Product
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * Product를 반환한다.
     * @param id Product의 신원을 확인하는 변수
     * @return id에 따른 Product
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * Product를 생성한다.
     * @param productData
     * @return
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * Product를 수정한다.
     * @param id Product의 신원을 확인하는 변수
     * @param productData 수정하고 싶은 Product 데이터
     * @return 변경된 Product를 반환한다.
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * Product를 삭제한다.
     * @param id id Product의 신원을 확인하는 변수
     * @return 삭제한 Product를 반환한다.
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
