package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.domain.ProductRepository;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
     * 상품 리스트를 가져와서 리턴한다.
     * @return 상품 리스트
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * 상품 id로 상품을 찾아 리턴한다.
     * @param id
     * @return 찾은 상품 정보
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * 상품을 추가한다.
     * @param productData
     * @return 생성한 상품 정보
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * id에 해당하는 상품을 찾아 수정한다.
     * @param id
     * @param productData
     * @return 수정한 상품 정보
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * id에 해당하는 상품을 찾아 삭제한다.
     * @param id
     */
    public Product deleteProduct(Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    /**
     * id에 해당하는 상품을 찾아 리턴한다.
     * @param id
     * @return 찾은 상품 정보
     * @throws id에 해당하는 상품을 찾지 못할 경우 ProductNotFoundException
     */
    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
