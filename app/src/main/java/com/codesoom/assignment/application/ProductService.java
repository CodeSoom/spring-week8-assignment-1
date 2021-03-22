package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductCreateData;
import com.codesoom.assignment.dto.ProductResultData;
import com.codesoom.assignment.dto.ProductUpdateData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/** 상룸에 대한 요청을 수행한다. */
@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 전체 상품 목록을 리턴한다.
     *
     * @return 저장되어 있는 전체 상품 목록
     */
    public List<ProductResultData> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResultData::of)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 상품을 저장하고 저장된 상품을 리턴한다.
     *
     * @param productCreateData - 저장하고자 하는 새로운 상품
     * @return 저장 된 상품
     */
    public ProductResultData createProduct(ProductCreateData productCreateData) {
        Product product = productCreateData.toEntity();

        Product savedProduct =  productRepository.save(product);

        return ProductResultData.of(savedProduct);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 수정하고 수정된 상품을 리턴한다.
     *
     * @param id - 수정하고자 하는 상품의 식별자
     * @param productUpdateData - 수정하고자 하는 새로운 상품
     * @return 수정 된 상품
     * @throws ProductNotFoundException 만약
     *         주어진  {@code id}에 해당하는 상품을 찾을 수 없는 경우
     */
    public ProductResultData updateProduct(Long id, ProductUpdateData productUpdateData) {
        Product product = getProduct(id);

        product.updateWith(productUpdateData);

        return ProductResultData.of(product);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 삭제하고 삭제된 상품을 리턴한다.
     *
     * @param id - 삭제하고자 하는 상품의 식별자
     * @return 삭제 된 상품
     * @throws ProductNotFoundException 만약
     *         주어진  {@code id}에 해당하는 상품이 저장되어 있지 않은 경우
     */
    public ProductResultData deleteProduct(Long id) {
        Product deletedProduct = getProduct(id);

        productRepository.delete(deletedProduct);

        return ProductResultData.of(deletedProduct);
    }

    /**
     * 주어진 식별자에 해당하는 상품을 리턴한다.
     *
     * @param id - 조회하고자 하는 상품의 식별자
     * @return 주어진 {@code id}에 해당하는 상품
     * @throws ProductNotFoundException 만약
     *         주어진 {@code id}에 해당하는 상품이 저장되어 있지 않은 경우
     */
    public Product getProduct(Long id) {
        return  productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
