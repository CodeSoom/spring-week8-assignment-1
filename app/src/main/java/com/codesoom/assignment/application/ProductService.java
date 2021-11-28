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
 * 상품의 생성, 조회, 수정 삭제를 담당한다.
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
     * 상품의 목록을 리턴한다.
     *
     * @return 상품 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * id에 해당하는 상품을 리턴한다.
     *
     * @param id 조회할 상품의 id
     * @return id에 해당하는 상품
     * @throws ProductNotFoundException 상품을 찾을 수 없는 경우
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 생성한 상품을 리턴한다.
     *
     * @param productData 상품 생성에 필요한 데이터
     * @return 생성한 상품
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 수정한 상품을 리턴한다.
     *
     * @param id 수정할 상품의 id
     * @param productData 수정할 데이터
     * @return 수정한 상품
     * @throws ProductNotFoundException 수정할 상품을 찾을 수 없는 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품을 삭제한다.
     *
     * @param id 삭제할 상품의 id
     * @return 삭제한 상품
     * @throws ProductNotFoundException 삭제할 상품을 찾을 수 없는 경우
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
