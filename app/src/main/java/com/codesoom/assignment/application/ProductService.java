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
 * 상품을 찾고, 수정하고, 저장하고, 삭제합니다.
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
     * 모든 상품 목록을 반환합니다.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 아이디로 상품을 찾아서 상품을 반환합니다.
     *
     * @param id 는 상품의 아이디 입니다.
     * @throws ProductNotFoundException 는 상품 아이디가 존재하지 않을 때 던집니다.
     */

    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 새로운 상품 데이터를 상품 목록에 저장합니다.
     * @param productData 는 등록하려는 상품 데이터 입니다.
     */

    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * 아이디로 수정할 상품을 찾아서 기존의 상품 데이터를 수정합니다.
     *
     * @param id 는 상품의 아이디 입니다.
     * @param productData 는 등록하려는 상품 데이터 입니다.
     *
     */

    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * 아이디로 삭제할 상품을 찾아서 해당 상품을 삭제합니다.
     *
     * @param id 는 상품의 아이디 입니다.
     */

    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * 아이디로 상품을 찾아서 해당 상품을 반환합니다.
     *
     * @param id 는 상품의 아이디 입니다.
     * @throws ProductNotFoundException 은 상품의 아이디를 찾지 못했을 때 던집니다.
     */

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
