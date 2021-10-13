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
 * 상품에 대한 서비스를 제공한다.
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
     * 상품 목록을 조회하여 반환한다.
     *
     * @return 상품 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 식별자로 상품 상세 정보를 조회 해 반환한다.
     *
     * @param id 식별자
     * @return 상품 상세 정보
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 새로 등록 한 뒤, 등록된 상품을 반환한다.
     *
     * @param productData 상품 정보
     * @return 등록된 상품 정보
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 식별자에 맞는 상품 정보를 수정한 뒤, 수정된 상품을 반환한다.
     *
     * @param id 식별자
     * @param productData 상품 정보
     * @return 수정된 상품 정보
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품 정보를 삭제 하고, 삭제 된 상품 정보를 반환한다.
     *
     * @param id 식별자
     * @return 삭제 된 상품 정보
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
