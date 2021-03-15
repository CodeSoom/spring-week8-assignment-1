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
 * Service for products.
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
     * Returns all products in this application.
     *
     * @return all products.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * Returns the products with given ID.
     *
     * @param id is identifier of the product.
     * @return the products with given ID.
     * @throws ProductNotFoundException in case any product doesn't exist with given ID.
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * Returns the saved product.
     *
     * @param productData is given data to save a product in this application.
     * @return the saved product.
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * Returns the modified product with given ID.
     *
     * @param id is identifier of the product to modify.
     * @param productData is given data to modify the product with given ID.
     * @return the modified product.
     * @throws ProductNotFoundException in case any product doesn't exist with given ID.
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * Returns the removed product in this application.
     *
     * @param id is identifier of the product to remove
     * @return the removed product.
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
