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
     * 상품 리스트를 가져와서 리턴한다.
     * @return 상품 리스트
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 상품 id로 상품을 찾아 리턴한다.
     * @param id
     * @return 찾은 상품 정보
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 상품을 추가한다.
     * @param productData
     * @return 생성한 상품 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * id에 해당하는 상품을 찾아 수정한다.
     * @param id
     * @param productData
     * @return 수정한 상품 정보
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
     * id에 해당하는 상품을 찾아 삭제한다.
     * @param id
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
