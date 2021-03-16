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
 * 장난감 서비스
 *
 * @author newoo (newoo4297@naver.com)
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
     * 애플리케이션에 있는 전체 장난감들을 반환한다.
     *
     * @return 전체 장난감들
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 식별자를 가지고 있는 장난감을 반환한다.
     *
     * @param id 장난감 식별자.
     * @return 주어진 식별자를 가지고 있는 장난감.
     * @throws ProductNotFoundException 주어진 식별자를 가진 장난감이 없다는 예외.
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 애플리케이션에 장난감을 저장한다.
     *
     * @param productData 애플리케이션에 저정할 장난감 데이터.
     * @return 저장된 장난감.
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 식별자를 가지고, 수정된 장난감을 반환한다.
     *
     * @param id 수정할 장난감의 식별자.
     * @param productData 애플리케이션에 수정할 장난감 데이터.
     * @return 수정된 장난감
     * @throws ProductNotFoundException 주어진 식별자를 가진 장난감이 없다는 예외.
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 애플리케이션에서 삭제된 장난감을 반환한다.
     *
     * @param id 삭제할 장난감의 식별자.
     * @return 삭제된 장난감
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
