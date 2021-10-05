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
 * 상품에 대한 Http Request 요청을 처리한다.
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
     * 상품 목록을 조회해 반환한다.
     *
     * @return 상품 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 식별자로 상품 정보를 조회해 반환한다.
     *
     * @param id 식별자
     * @return 상품 정보
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 올바른 인증 정보를 가지고 있는 회원이 요청한 경우,
     * 상품을 등록한 뒤, 등록된 상품을 반환한다.
     *
     * @param productData 상품 정보
     * @return 등록 된 상품 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 올바른 인증 정보를 가지고 있는 회원이 요청한 경우,
     * 식별자로 회원 정보를 찾아, 회원 정보를 수정 후 수정 된 회원 정보를 반환한다.
     *
     * @param id 수정 할 회원 식별자
     * @param productData 수정 할 회원 정보
     * @return 수정 된 회원 정보
     * @see
     *
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
     * 올바른 인증 정보를 가지고 있는 회원이 요청한 경우,
     * 식별자로 회원 정보를 찾아, 회원 정보를 삭제한다.
     *
     * @param id 삭제할 회원 식별자
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
