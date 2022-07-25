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
 * Product 에 대한 HTTP 요청 처리
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
     * 모든 Product 목록 반환하고, 200 상태코드 응답
     *
     * @return 저장된 모든 Product 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * id 에 해당하는 Product 조회하여 반환하고, 200 상태코드 응답
     *
     * @param id Product 식별자
     * @return 주어진 id 와 일치하는 Product
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * Product 를 등록하고, 201 상태코드 응답
     *
     * @param productData 등록할 Product
     * @return 등록된 Product
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * id 에 해당하는 Product 를 수정한 후 반환하고, 200 상태코드 응답
     *
     * @param id 수정할 Product 를 가지고 있는 id
     * @param productData 수정할 Product
     * @return 수정된 Product
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
     * id 에 해당하는 Product 를 삭제하고, 204 상태코드 응답
     *
     * @param id 삭제할 Product 를 가지고 있는 id
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
