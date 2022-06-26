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
 * Product 에 대한 비즈니스 로직
 */
@Service
@Transactional
public class ProductService {
    private final Mapper mapper;
    /**
     * Product 데이터 저장소
     */
    private final ProductRepository productRepository;

    public ProductService(
            Mapper dozerMapper,
            ProductRepository productRepository
    ) {
        this.mapper = dozerMapper;
        this.productRepository = productRepository;
    }

    /**
     * 저장된 모든 Product 목록을 반환
     *
     * @return 저장된 모든 Product 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 주어진 id 와 일치하는 Product 를 찾아서 반환
     *
     * @param id Product 식별자
     * @return 주어진 id 와 일치하는 Product
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 주어진 Product 를 저장하고 저장된 Product 를 반환
     *
     * @param productData 저장할 Product
     * @return 저장된 Product
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 주어진 id 와 일치하는 Product 를 수정하고 수정된 Product 반환
     *
     * @param id Product 식별자
     * @param productData 수정할 Product
     * @return 수정된 Product
     * @throws ProductNotFoundException Product 를 찾을 수 없는 경우
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 주어진 id 와 일치하는 Product 를 삭제하고 삭제된 Product 반환
     *
     * @param id Product 식별자
     * @return 삭제한 Product
     * @throws ProductNotFoundException Product 를 찾을 수 없는 경우
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 주어진 id 와 일치하는 Product 를 찾을 수 있으면 반환하고,
     * 찾을 수 없다면 Product 를 찾을 수 없다는 예외를 던진다.
     *
     * @param id Product 식별자
     * @return 예외 처리
     * @throws ProductNotFoundException 주어진 id 와 일치하는 Product 를 찾을 수 없는 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
