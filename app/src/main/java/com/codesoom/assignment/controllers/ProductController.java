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
 * Controller For Product
 *
 * @author sim
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private final AuthenticationService authenticationService;

    /**
     * ProductController에 대한 생성자 메서드
     * 
     * @param productService - 제품 서비스
     * @param authenticationService - 인증 서비스
     */
    public ProductController(ProductService productService,
                             AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    /**
     * 모든 제품을 조회한다.
     * 
     * @return 제품 리스트
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 제품에 대한 상세조회를 한다.
     * 
     * @param id - 제품 고유 식별 ID
     * @return 제품 정보
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 제품을 생성한다.
     * 메서드 실행 전 인증 여부 및 USER 인가 권한을 체크하고, 실패 시 401 에러를 응답한다.
     *
     * @param productData - 등록할 제품 정보 게체
     * @return 등록된 제품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 제품을 수정한다.
     * 메서드 실행 전 인증 여부를 체크한다. 실패 시 401 에러를 응답한다.
     *
     * @param id - 제품 고유 식별 ID
     * @param productData - 수정할 제품 정보
     * @return 수정된 제품
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
     * 제품을 삭제한다.
     * 메서드 실행 전 인증 여부를 체크한다. 실패 시 401 에러를 응답한다.
     *
     * @param id - 제품 고유 식별 ID
     */
    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
