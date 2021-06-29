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
 * Service for Product (CRUD)
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
     * Returns all products in application.
     * @return all products.
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * Returns the Product with given ID.
     * @param id  ID is the identifier of the  products
     * @return the product with give ID. Product is Not Null
     */
    public Product getProduct(Long id) {
        return findProduct(id);
    }

    /**
     * Creates  a given Product. Returns created Product.
     * @param productData productData is  created Product
     * @return the product with created Product.
     */
    public Product createProduct(ProductData productData) {
        Product product = mapper.map(productData, Product.class);
        return productRepository.save(product);
    }

    /**
     * Update a given Product. Returns updated Product.
     * @param id  ID is the identifier of the  products
     * @param productData productData is  updated Product
     * @return the product with updated Product.
     * @throws ProductNotFoundException in case Product with given ID is not existed.
     */
    public Product updateProduct(Long id, ProductData productData) {
        Product product = findProduct(id);

        product.changeWith(mapper.map(productData, Product.class));

        return product;
    }

    /**
     * Delete a given Product. Returns deleted Product.
     * @param id ID is the identifier of the  products
     * @return the product with deleted Product.
     * @throws ProductNotFoundException in case Product with given ID is not existed.
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
