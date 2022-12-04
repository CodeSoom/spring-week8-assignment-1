package com.codesoom.assignment.product.domain;

import com.codesoom.assignment.product.domain.port.command.ProductCreateRequest;
import com.codesoom.assignment.product.domain.port.command.ProductUpdateRequest;
import com.codesoom.assignment.product.domain.port.in.ProductUseCase;
import com.codesoom.assignment.product.exception.ProductNotFoundException;
import com.codesoom.assignment.product.repository.Product;
import com.codesoom.assignment.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService implements ProductUseCase {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(final Long id) {
        return findProduct(id);
    }

    public Product createProduct(final ProductCreateRequest productCreateRequest) {
        return productRepository.save(productCreateRequest.toEntity());
    }

    public Product updateProduct(final Long id,
                                 final ProductUpdateRequest productUpdateRequest) {
        Product product = findProduct(id);

        product.update(productUpdateRequest.toEntity());

        return product;
    }

    public Product deleteProduct(final Long id) {
        Product product = findProduct(id);

        productRepository.delete(product);

        return product;
    }

    private Product findProduct(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }
}
