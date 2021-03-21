package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.errors.ProductNotFoundException;
import com.github.dozermapper.core.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 전체 상품을 응답합니다.
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 주어진 id에 해당하는 상품을 응답합니다.
     *
     * @param id 찾고자 하는 상품의 id
     * @throws ProductNotFoundException 주어진 id가 존재하지 않을 때
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 상품을 생성합니다.
     *
     * @param productData 생성 할 상품의 정보
     * @return 생성된 상품의 정보
     * @throws MappingException 올바르지 않은 상품의 정보가 주어졌을 때
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 상품의 정보를 변경합니다.
     *
     * @param id          변경하고자 하는 상품의 id
     * @param productData 변경하고자 하는 정보
     * @return 변경된 상품의 정보
     * @throws ProductNotFoundException 주어진 id가 존재하지 않을 때
     * @throws MappingException         올바르지 않은 상품의 정보가 주어졌을 때
     */
    @PatchMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 상품을 삭제합니다.
     *
     * @param id 삭제하고자 하는 상품의 id
     * @throws ProductNotFoundException 주어진 id가 존재하지 않을 때
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
