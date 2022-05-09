package com.codesoom.assignment.controller.products;

import com.codesoom.assignment.application.auth.AuthorizationService;
import com.codesoom.assignment.application.products.ProductDeleteService;
import com.codesoom.assignment.config.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 상품 삭제 요청을 처리합니다.
 */
@ProductController
public class ProductDeleteController {

    private final ProductDeleteService service;

    public ProductDeleteController(ProductDeleteService service) {
        this.service = service;
    }

    /**
     * 식별자로 찾은 상품을 삭제합니다.
     *
     * @param id 상품 식별자
     */
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }

}
