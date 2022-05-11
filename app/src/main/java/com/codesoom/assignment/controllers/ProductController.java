package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.common.CommonResponse;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 클라이언트로 부터 crud 요청과 요청에 대한 응답을 처리하는 controller입니다
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
     * 등록된 모든 상품 목록과 상태코드 200을 응답합니다
     *
     * @return 모든 상품 목록
     */
    @GetMapping
    public CommonResponse<List<Product>> list() {
        List<Product> list = productService.getProducts();
        return CommonResponse.success(list);
    }

    /**
     * 등록된 상품중 id에 해당하는 상품 상세 정보와 상태코드 200을 응답합니다.
     *
     * @param id 상품의 id
     * @return id에 해당하는 상품의 상세 정보
     */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 로그인이 되어있고, 권한이 USER인 경우
     * 새로운 상품 등록 요청을 처리합니다.
     * 상품을 등록하면 상태코드 201과 등록된 상품을 응답합니다
     *
     * @param productData 등록하고자 하는 상품의 정보
     * @return 등록된 상품정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(@RequestBody @Valid ProductData productData) {
        return productService.createProduct(productData);
    }

    /**
     * 로그인이 되어 있고, 등록된 상품중 id에 해당하는 상품의 정보를 수정하려는 요청을 처리합니다.
     * 상태코드 200과 수정된 상품 정보를 응답합니다.
     *
     * @param id          상품의 id
     * @param productData 수정하려는 상품 정보
     * @return 수정된 상품 정보
     */
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 로그인이 되어 있고,  등록된 상품중 id에 해당하는 상품 삭제 요청을 처리합니다
     * 상태코드 204을 응답합니다
     *
     * @param id 상품 id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void destroy(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
