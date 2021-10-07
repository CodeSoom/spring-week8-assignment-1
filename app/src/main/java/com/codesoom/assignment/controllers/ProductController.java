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
 * 상품에 대한 HTTP 요청을 받아서 서비스 객체에 전달합니다.
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
     * 모든 상품 목록을 조회하는 요청을 받아서 서비스에서 상품 목록을 반환합니다.
     */

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 상품의 세부 정보를 조회하는 요청을 받아서 서비스에서 해당 상품을 찾고 데이터를 반환합니다.
     * @param id 는 상품의 아이디
     */

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 상품을 등록하라는 요청을 받아서 서비스에서 상품을 등록하고 반환합니다.
     * @param productData 등록하려는 상품의 데이터
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 상품의 정보를 업데이트 하라는 요청을 받고, 서비스에서 수정한 상품을 반환합니다.
     *
     * @param id 상품의 아이디
     * @param productData 등록하려는 상품의 데이터
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
     * 상품을 삭제하라는 요청을 받고, 서비스에서 상품을 삭제합니다.
     * @param id 상품의 아이디
     */

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
