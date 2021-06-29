package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
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

/**
 * 프로덕트 관련 요청을 처리하고 그에 따른 응답을 담당합니다.
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
     * 모든 프로덕트 목록을 반환합니다.
     *
     * @return 모든 프로덕트 리스트
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 특정한 프로덕트을 반환합니다.
     *
     * @param id 프로덕트 식별자
     * @return 프로덕트
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 요청 프로덕트을 등록하고, 등록한 프로덕트을 반환합니다.
     *
     * @param productData 신규 등록할 프로덕트
     * @return 등록한 프로덕트
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 특정한 프로덕트 정보를 갱신하고, 갱신한 프로덕트을 반환합니다.
     * 
     * @param id 프로덕트 식별자
     * @param productData 갱신할 프로덕트
     * @return 갱신한 프로덕트
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
     * 특정한 프로덕트을 삭제합니다.
     * @param id 프로덕트 식별자
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
