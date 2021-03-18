package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 상품 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/products")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final AuthenticationService authenticationService;

    /**
     * 전체 상품 목록을 반환합니다.
     *
     * @return 전체 상품 목록
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 주어진 id에 해당하는 상품의 상세 정보를 반환합니다.
     *
     * @param id 상품의 식별자
     * @return 상품
     */
    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 새로운 상품을 등록합니다.<br/>
     * 'USER' 권한을 가진 인증된 사용자만 상품을 등록할 수 있습니다.<br/>
     *
     * @param productData 등록할 상품 정보
     * @param authentication 사용자 인증 정보
     * @return 등록된 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public Product create(
            @RequestBody @Valid ProductData productData,
            Authentication authentication
            ) {
        return productService.createProduct(productData);
    }

    /**
     * 주어진 id에 해당하는 상품의 정보를 수정합니다.<br/>
     * 인증된 사용자만 상품의 정보를 수정할 수 있습니다.
     *
     * @param id 수정할 상품의 식별자
     * @param productData 수정할 상품 정보
     * @param authentication 사용자 인증 정보
     * @return 수정된 상품
     */
    @RequestMapping(value = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @PreAuthorize("isAuthenticated()")
    public Product update(
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData,
            Authentication authentication
            ) {
        return productService.updateProduct(id, productData);
    }

    /**
     * 주어진 id에 해당하는 상품을 삭제합니다.<br/>
     * 인증된 사용자만 상품을 삭제할 수 있습니다.
     *
     * @param id 삭제할 상품의 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    /**
     * Authorization 헤더에 대한 예외를 처리합니다.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public void handleMissingRequestHeaderException() {
        //
    }
}
