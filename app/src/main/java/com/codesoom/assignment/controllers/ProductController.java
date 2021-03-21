package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 상품 HTTP 요청 핸들러.
 */
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
     * 존재하는 모든 상품 목록을 응답합니다.
     *
     * @return 상품 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 대응되는 식별자의 상품을 응답합니다.
     *
     * @param id 상품 식별자
     * @return 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 주어진 상품 정보로 새로운 상품을 생성하고 응답합니다.
     *
     * @param productData 생성 상품 정보
     * @return 생성된 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 대응되는 식별자의 상품을 주어진 상품 정보로 수정하고 응답합니다.
     *
     * @param id          상품 식별자
     * @param productData 수정 상품 정보
     * @return 수정된 상품
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
     * 대응되는 식별자의 상품을 삭제합니다.
     *
     * @param id 상품 식별자
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
