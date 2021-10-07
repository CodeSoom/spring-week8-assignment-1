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
 * products에 대한 HTTP 요청을 처리한다.
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
     * products의 get 요청을 처리하고 List<Product>를 응답
     *
     * @return List<Product> 상품 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * products/{id}에 대한 get 요청을 처리하고 id에 해당하는 Product를 응답
     *
     * @param id 상품의 식별자
     * @return 식별자에 해댱하는 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * products에 대한 post 요청을 처리하고 등록된 Product 응답
     *
     * @param productData 등록할 상품 정보
     * @return 등록된 상품 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        // location reponse header
        return productService.createProduct(productData);
    }

    /**
     * products/{id}에 대한 patch 요청을 처리하고 수정한 Product를 응답
     *
     * @param id Product의 식별자
     * @param productData 수정할 Product의 목표 정보
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
     * products/{id}에 대한 delete 요청을 처리
     *
     * @param id Product 식별자
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
