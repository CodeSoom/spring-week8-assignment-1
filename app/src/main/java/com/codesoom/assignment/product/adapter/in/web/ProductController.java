package com.codesoom.assignment.product.adapter.in.web;

import com.codesoom.assignment.product.adapter.in.web.dto.request.ProductData;
import com.codesoom.assignment.product.application.ProductService;
import com.codesoom.assignment.product.domain.Product;
import com.codesoom.assignment.session.application.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private final AuthenticationService authenticationService;

    public ProductController(ProductService productService,
                             AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
