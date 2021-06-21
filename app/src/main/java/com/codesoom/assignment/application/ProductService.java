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
 * 프로덕트에 관한 유스케이스를 담당합니다.
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
     * 모든 프로덕트를 반환합니다.
     *
     * @return 모든 프로덕트가 담긴 리스트
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 특정한 프로덕트를 가져옵니다.
     *
     * @param id 가져오려는 프로덕트의 아이디
     * @return 가져온 프로덕트
     * @throws ProductNotFoundException 프로덕트를 못찾은 경우
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 프로덕트를 생성합니다.
     *
     * @param productData 생성할 프로덕트의 정보
     * @return 생성된 프로덕트
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 프로덕트의 정보를 변경합니다.
     *
     * @param id 변경할 프로덕트의 아이디
     * @param productData 변경할 프로덕트 정보
     * @return 변경된 프로덕트
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 프로덕트를 삭제합니다.
     *
     * @param id 삭제할 대상의 아이디
     * @return 삭제된 프로덕트
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 전달된 식별자에 해당하는 프로덕트를 찾습니다.
     *
     * @param id 프로덕트 식별자
     * @throws ProductNotFoundException 프로덕트를 찾을 수 없는 경우
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
