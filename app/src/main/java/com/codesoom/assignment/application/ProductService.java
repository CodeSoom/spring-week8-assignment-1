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
 * Products의 Service
 *
 * @author 혁 (pjh08190819@gmail.com)
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
     * 모든 Products를 리턴합니다.
     *
     * @return 모든 Products
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id를 가진 Product를 리턴합니다.
     *
     * @param id Product의 식별자
     * @return 주어진 id를 가진 Product
     * @throws ProductNotFoundException 주어진 id의 Product가 존재하지 않는 경우
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 주어진 ProductData를 저장하고, 저장된 Product를 리턴합니다.
     *
     * @param productData 저장될 Product 정보
     * @return 저장된 Product
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 id에 만족하는 Product를 ProductData로 수정하고, 수정된 Product를 리턴합니다.
     *
     * @param id 수정될 Product의 식별자
     * @param productData 수정될 Product 데이터
     * @return 수정된 Product
     * @throws ProductNotFoundException 주어진 id의 Product가 존재하지 않는 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 id에 만족하는 Product를 삭제합니다.
     *
     * @param id 삭제될 Product의 식별자
     * @return 삭제된 Product
     * @throws ProductNotFoundException 주어진 id의 Product가 존재하지 않는 경우
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
