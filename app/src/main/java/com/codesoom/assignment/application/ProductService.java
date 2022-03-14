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
 * 상품의 조회, 생성, 수정, 삭제를 담당하는 서비스 입니다.
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
     * 전체 상품을 리턴합니다.
     *
     * @return 상품 목록
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * id를 받아 해당 id의 상품을 리턴합니다.
     *
     * @param id 상품 id
     * @return 입력받은 id의 상품
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품 정보를 받아 새로운 상품을 생성하여 리턴합니다.
     *
     * @param productData 추가하려는 상품 정보
     * @return 새로 추가된 상품 정보
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 상품 목록 중 id가 일치하는 상품을 찾고,
     * 이를 수정된 정보로 업데이트 하여 수정된 정보를 리턴합니다.
     *
     * @param id 상품 id
     * @param productData 수정하려는 상품 정보
     * @return 수정된 상품 정보
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 상품 목록 중 id의 상품을 삭제합니다.
     *
     * @param id 상품 id
     * @return 삭제된 상품 정보
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
