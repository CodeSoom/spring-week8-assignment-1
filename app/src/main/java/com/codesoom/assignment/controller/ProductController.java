package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import com.codesoom.assignment.error.InvalidTokenException;
import com.codesoom.assignment.error.ProductNotFoundException;
import com.codesoom.assignment.error.UserNotFoundException;
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
     * 모든 상품을 조회한다.
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 상품을 조회한다.
     *
     * @param id 조회할 상품의 식별자
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @return 식별자에 해당하는 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 상품을 저장한다.
     *
     * @param productData 상품 저장 정보
     * @throws InvalidTokenException 유효하지 않은 토큰일 경우
     * @throws UserNotFoundException 사용자가 존재하지 않을 경우
     * @return 저장된 상품의 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 상품을 수정한다.
     *
     * @param id 수정할 상품의 식별자
     * @param productData 상품 수정 정보
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
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
     * 상품을 삭제한다.
     *
     * @throws ProductNotFoundException 식별자에 해당하는 상품이 존재하지 않을 경우
     * @param id 삭제할 상품의 식별자
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
